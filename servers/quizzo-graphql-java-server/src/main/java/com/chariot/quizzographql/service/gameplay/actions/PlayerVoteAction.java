package com.chariot.quizzographql.service.gameplay.actions;

import com.chariot.quizzographql.models.PlayerScore;
import com.chariot.quizzographql.models.PlayerScores;
import com.chariot.quizzographql.models.Question;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.service.gameplay.QuizDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlayerVoteAction implements Action<GameStates, GameEvents> {

    private QuizDataService quizDataService;

    @Autowired
    public PlayerVoteAction(QuizDataService quizDataService) {
        this.quizDataService = quizDataService;
    }

    private final static Log logger = LogFactory.getLog(PlayerVoteAction.class);

    @Override
    public void execute(StateContext<GameStates, GameEvents> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();

        String player = (String) context.getMessageHeader("player");
        String answer = (String) context.getMessageHeader("answer");
        Quiz quiz = (Quiz) variables.get("quiz");

        String quizRunId = (String) variables.get("quizRunId");

        if (answer == null) {
            logger.info("Player submitted answer request with no answer payload");
            throw new RuntimeException("Vote failed - no valid vote sent");
        }
        Question question =
            (Question) context.getExtendedState().getVariables().getOrDefault("currentQuestion", null);

        if (question == null) {
            logger.info("Quiz is not defined, this action is executing at the wrong time.");
            return;
        }

        Integer questionId = question.getId();

        if (quizDataService.hasAnswerForPlayer(quizRunId, question.getId(), player)) {
            logger.warn("Player has already voted.");
        } else {
            // get seed of score entry
            PlayerScore playerScore = question.scoreAnswerForQuestion(question, answer);

            playerScore.setQuestionId(questionId);

            // we have this here in workflow
            playerScore.setPlayerId(player);

            quizDataService.savePlayerScore(quizRunId, quiz, playerScore); // persist here
        }
    }
}
