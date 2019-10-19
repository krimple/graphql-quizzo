package com.chariot.quizzographql.service;

import com.chariot.quizzographql.models.Question;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import com.chariot.quizzographql.repository.PlayerScoreReportService;
import com.chariot.quizzographql.security.SecurityUtils;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.service.gameplay.QuizDataService;
import com.chariot.quizzographql.service.gameplay.QuizMachineFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InMemoryGamePlayService implements GamePlayService {

    private final Log logger = LogFactory.getLog(this.getClass());

    private QuizMachineFactory quizMachineFactory;

    // TODO - move this out of here?
    private QuizDataService quizDataService;

    private PlayerScoreReportService reportService;

    @Autowired
    public InMemoryGamePlayService(
            QuizMachineFactory quizMachineFactory,
            QuizDataService quizDataService,
            PlayerScoreReportService reportService) {
        this.quizMachineFactory = quizMachineFactory;
        this.quizDataService = quizDataService;
        this.reportService = reportService;
    }

    @Override
    public GameStates getCurrentGameState() {
        if (quizMachineFactory.getStateMachine() != null) {
            if (quizMachineFactory.getStateMachine() != null) {
                return quizMachineFactory.getStateMachine().getState().getId();
            }
        }

        // in any other case, the game machine isn't running yet
        return GameStates.NOT_RUNNING;
    }

    @Override
    public boolean playerExists(String nickName) {
        return false;
    }

    @Override
    public Question getCurrentQuestion() {
        return (Question)this.quizMachineFactory.getStateMachine().getExtendedState().getVariables().getOrDefault("currentQuestion", null);
    }

    @Override
    public Quiz getCurrentQuiz() {
        if (this.quizMachineFactory == null || this.quizMachineFactory.getStateMachine() == null) {
            return null;
        }
        return (Quiz) this.quizMachineFactory.getStateMachine().getExtendedState().getVariables().getOrDefault("quiz", null);
    }

    /**
     * Caller should get confirmation or not of quiz creation failing
     * @param quizId
     * @return
     */
    @Override
    public boolean loadQuizAndBeginRegistration(Long quizId) {
        if (this.quizMachineFactory == null) {
            logger.debug("Can't reset state machine - factory not initialized. Ignored.");
            return false;
        }

        this.quizMachineFactory.resetStateMachine();

        try {
            Map<String, Object> headers = new HashMap<>();
            headers.put("quizId", quizId);
            // action loads question into state
            this.quizMachineFactory.newGameInstance(quizId);
            return true;
        } catch (Exception e) {
            logger.error("Cannot create new quiz instance");
            logger.error(e);
            return false;
        }
    }

    @Override
    public boolean registerPlayerForCurrentGame() {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete() ||
            this.quizMachineFactory.getStateMachine().getState().getId() != GameStates.AWAITING_PLAYERS) {
            logger.debug("Can't register at this time.");
            return false;
        }

        String user = SecurityUtils.getCurrentPrincipalName();
        if (user == null) {
            logger.debug("No user in security context. Cannot register.");
            return false;
        }
        logger.info("Requesting join of " + user + " to current game.");
        Map<String, Object> headers = new HashMap<>();
        headers.put("playerId", user);
        Message message = MessageBuilder.createMessage(GameEvents.REGISTER_PLAYER, new MessageHeaders(headers));
        return this.quizMachineFactory.getStateMachine().sendEvent(message);
    }

    public boolean endRegistrationAndBeginGamePlay() {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete()) {
            logger.debug("Can't end signon because not initialized or running. Ignored.");
            return false;
        }
        return this.quizMachineFactory.getStateMachine().sendEvent(GameEvents.BEGIN_GAMEPLAY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PlayerScoreReportEntry> getScoresForQuestion() {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null) {
            logger.debug("State machine not initialized. Cannot get scores.");
        }

        String quizRunId = (String)this.quizMachineFactory.getStateMachine().getExtendedState().getVariables().get("quizRunId");
        Question question = (Question)this.quizMachineFactory.getStateMachine().getExtendedState().getVariables().get("currentQuestion");

        List<PlayerScoreReportEntry> scores =
                this.reportService.playerScoresForQuestion(quizRunId, question.getId());

        return scores;
    }

    @Override
    public PlayerScoreReportEntry getPlayerScoreForQuestion() {
        String playerName = SecurityUtils.getCurrentPrincipalName();
        if (this.quizMachineFactory == null ||
                this.quizMachineFactory.getStateMachine() == null) {
            logger.debug("State machine not initialized. Cannot get scores.");
        }
        String quizRunId = (String)this.quizMachineFactory.getStateMachine().getExtendedState().getVariables().get("quizRunId");
        Question question = (Question)this.quizMachineFactory.getStateMachine().getExtendedState().getVariables().get("currentQuestion");
        // TODO - fix guards above to be better and also add guard to check on whether question is defined
        if (question != null) {
            PlayerScoreReportEntry score = reportService.playerScoresForQuestionAndPlayer(quizRunId, playerName, question.getId());
            return score;
        } else {
            return null;
        }
    }

    public boolean endTurn() {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete()) {
            logger.debug("Can't end turn if state machine not initialized or running. Ignored.");
            return false;
        }
        return this.quizMachineFactory.getStateMachine().sendEvent(GameEvents.END_TURN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinalPlayerScore> generateFinalScoresForCurrentQuizInstance() {
         if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete()) {
             logger.warn("Can't generate scores if state machine not initialized or running. Ignored.");
         }

        Map<Object, Object> variables = this.quizMachineFactory.getStateMachine().getExtendedState().getVariables();
        String quizInstanceId = (String)variables.getOrDefault("quizRunId", null);

        return this.quizDataService.fetchFinalScores(quizInstanceId);
    }

    @Override
    public List<FinalPlayerScore> getFinalScoresForCurrentQuizInstance() {
         if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().getState().getId() != GameStates.GAME_OVER) {
             logger.debug("Can't display scores if state machine not initialized or running. Ignored.");
             return new ArrayList<FinalPlayerScore>();
         }

         Map<Object, Object> variables = this.quizMachineFactory.getStateMachine().getExtendedState().getVariables();

         String quizInstanceId = (String)variables.getOrDefault("quizRunId", null);
         return this.reportService.finalScoresForGame(quizInstanceId);
    }

    public boolean nextQuestion() {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete()) {
            logger.debug("Can't request next question if state machine not initialized or running. Ignored.");
            return false;
        }
        return this.quizMachineFactory.getStateMachine().sendEvent(GameEvents.NEXT_QUESTION);
    }

    public boolean answerQuestion(String playerName, String answer) {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete()) {
            logger.debug("Can't answer question if state machine not initialized or running. Ignored.");
            return false;
        }
        if (this.quizMachineFactory.getStateMachine().getState().getId() == GameStates.PRESENTING_QUESTION) {
            Map<String, Object> messageHeaders = new HashMap<>();
            // TODO add helper somewhere and/or simplify
            messageHeaders.put("player", playerName);
            messageHeaders.put("answer", answer);
            Message message = MessageBuilder.createMessage(GameEvents.ANSWER_QUESTION, new MessageHeaders(messageHeaders));
            this.quizMachineFactory.getStateMachine().sendEvent(message);
            logger.debug("Answering question with " + answer);
            return true;
        } else {
            logger.debug("people, people... It's not the right time.");
            return false;
        }
    }

    public boolean isCurrentQuestionAnswered(String playerNickName) {
        if (this.quizMachineFactory == null ||
            this.quizMachineFactory.getStateMachine() == null ||
            this.quizMachineFactory.getStateMachine().isComplete()) {
            logger.debug("Can't answer question if state machine not initialized or running. Ignored.");
            return false;
        }
        Map<Object, Object> variables = this.quizMachineFactory.getStateMachine().getExtendedState().getVariables();
        String quizInstanceId = (String)variables.getOrDefault("quizRunId", null);
        Question currentQuestion = (Question)variables.getOrDefault("currentQuestion", null);
        if (quizInstanceId != null && currentQuestion != null) {
           return quizDataService.hasAnswerForPlayer(quizInstanceId, currentQuestion.getId(), playerNickName);
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        this.quizMachineFactory.resetStateMachine();
    }

}
