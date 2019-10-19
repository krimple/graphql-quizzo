package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.graphql.graphmodels.ChoiceOption;
import com.chariot.quizzographql.graphql.graphmodels.CurrentQuestion;
import com.chariot.quizzographql.graphql.graphmodels.GameStatus;
import com.chariot.quizzographql.graphql.graphmodels.PanelGameStatus;
import com.chariot.quizzographql.models.Option;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import com.chariot.quizzographql.security.SecurityUtils;
import com.chariot.quizzographql.service.GamePlayService;
import com.chariot.quizzographql.service.gameplay.GameStates;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service is called by players to get the current game status.
 */
@Service
public class PanelGameStatusFetcher implements DataFetcher<PanelGameStatus> {

    private GamePlayService gamePlayService;

    @Autowired
    public PanelGameStatusFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public PanelGameStatus get(DataFetchingEnvironment environment) throws Exception {
        GameStates gameState = gamePlayService.getCurrentGameState();
        if (gameState == null) {
            return new PanelGameStatus("NOT_RUNNING");

        }
        PanelGameStatus reportedGameStatus = new PanelGameStatus(gameState.name());

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
            List<PlayerScoreReportEntry> scores = this.gamePlayService.getScoresForQuestion();
            reportedGameStatus.setQuestionScores(scores);
        }

        if (gameState == GameStates.GAME_OVER) {
            List<FinalPlayerScore> finalScores = this.gamePlayService.getFinalScoresForCurrentQuizInstance();
            reportedGameStatus.setFinalScores(finalScores);
        }

        return reportedGameStatus;
    }
}
