package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import com.chariot.quizzographql.security.SecurityUtils;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.graphql.graphmodels.ChoiceOption;
import com.chariot.quizzographql.graphql.graphmodels.GameStatus;
import com.chariot.quizzographql.graphql.graphmodels.CurrentQuestion;
import com.chariot.quizzographql.models.Option;
import com.chariot.quizzographql.service.GamePlayService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This service is called by players to get the current game status.
 */
@Service
public class GameStatusFetcher implements DataFetcher<GameStatus> {

    private GamePlayService gamePlayService;

    @Autowired
    public GameStatusFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public GameStatus get(DataFetchingEnvironment environment) throws Exception {
        GameStates gameState = gamePlayService.getCurrentGameState();
        if (gameState == null) {
            return new GameStatus("NOT_RUNNING");

        }
        GameStatus reportedGameStatus = new GameStatus(gameState.name());

        // TODO: too many "over" states
        if (gameState != GameStates.IDLE &&
                gameState != GameStates.GAME_OVER &&
                gameState != GameStates.NOT_RUNNING &&
                gameState != GameStates.NOT_FOUND) {

            Quiz quiz = gamePlayService.getCurrentQuiz();
            if (quiz != null) {
                reportedGameStatus.setGameTitle(quiz.getTitle());
                reportedGameStatus.setGameDescription(quiz.getDescription());
            }
        }

        if (gameState == GameStates.PRESENTING_QUESTION || gameState == GameStates.PRESENTING_SCORES) {
            CurrentQuestion currentQuestion = new CurrentQuestion();
            currentQuestion.setText(gamePlayService.getCurrentQuestion().getText());

            List<ChoiceOption> options = new ArrayList<>();
            for (Option option : gamePlayService.getCurrentQuestion().getOptions()) {
                options.add(new ChoiceOption(option.getKey(), option.getLabel()));
            }

            reportedGameStatus.setCurrentOptions(options);
            reportedGameStatus.setCurrentQuestion(currentQuestion);

            // NOTE - we do report the score in both phases, just don't display until the score totals are displayed
            PlayerScoreReportEntry score = this.gamePlayService.getPlayerScoreForQuestion();
            if (score.getAnswer() != null) {
                reportedGameStatus.setQuestionScore(score);
            }
        }
        if (gameState == GameStates.GAME_OVER) {
            List<FinalPlayerScore> finalScores = this.gamePlayService.getFinalScoresForCurrentQuizInstance();
            // TODO - maybe push the user name check into the service?
            // TODO - remove playerNickName from this since we only care about the amount?
            Optional<FinalPlayerScore> finalScore =
                    finalScores
                            .stream()
                            .filter(score -> score.getNickName().equals(SecurityUtils.getCurrentPrincipalName())).findFirst();
            if (finalScore.isPresent()) {
                reportedGameStatus.setFinalScore((int)finalScore.get().getScore());
            }
        }

        logger.debug("Game State: " + reportedGameStatus.toString());

        return reportedGameStatus;
    }
}
