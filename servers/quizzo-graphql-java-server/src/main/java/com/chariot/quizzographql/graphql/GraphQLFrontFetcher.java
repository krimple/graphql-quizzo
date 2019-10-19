package com.chariot.quizzographql.graphql;

import com.chariot.quizzographql.graphql.fetchers.*;
import com.chariot.quizzographql.graphql.graphmodels.GameStatus;
import com.chariot.quizzographql.graphql.graphmodels.PanelGameStatus;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import graphql.schema.DataFetcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphQLFrontFetcher {
    private final Log logger = LogFactory.getLog(this.getClass());

    private LoginFetcher loginFetcher;

    private SelectGameFetcher selectGameFetcher;

    private BeginGamePlayFetcher beginGamePlayFetcher;

    private GetQuizzesFetcher getQuizzesFetcher;

    private GameStatusFetcher gameStatusFetcher;

    private PanelGameStatusFetcher panelGameStatusFetcher;

    private PlayerJoinCurrentGameFetcher playerJoinCurrentGameFetcher;

    private ResetFetcher resetFetcher;

    private PingFetcher pingFetcher;

    private MeFetcher meFetcher;

    private AnswerQuestionFetcher answerQuestionFetcher;

    private EndTurnFetcher endTurnFetcher;

    private NextQuestionFetcher nextQuestionFetcher;

    private RegisterPlayerFetcher registerPlayerFetcher;

    @Autowired
    public GraphQLFrontFetcher(LoginFetcher loginFetcher,
                               SelectGameFetcher selectGameFetcher,
                               BeginGamePlayFetcher beginGamePlayFetcher,
                               GetQuizzesFetcher getQuizzesFetcher,
                               GameStatusFetcher gameStatusFetcher,
                               PanelGameStatusFetcher panelGameStatusFetcher,
                               PlayerJoinCurrentGameFetcher playerJoinCurrentGameFetcher,
                               ResetFetcher resetFetcher,
                               PingFetcher pingFetcher,
                               MeFetcher meFetcher,
                               EndTurnFetcher endTurnFetcher,
                               NextQuestionFetcher nextQuestionFetcher,
                               AnswerQuestionFetcher answerQuestionFetcher,
                               RegisterPlayerFetcher registerPlayerFetcher) {
        this.loginFetcher = loginFetcher;
        this.selectGameFetcher = selectGameFetcher;
        this.beginGamePlayFetcher = beginGamePlayFetcher;
        this.getQuizzesFetcher = getQuizzesFetcher;
        this.gameStatusFetcher = gameStatusFetcher;
        this.panelGameStatusFetcher = panelGameStatusFetcher;
        this.playerJoinCurrentGameFetcher = playerJoinCurrentGameFetcher;
        this.resetFetcher = resetFetcher;
        this.pingFetcher = pingFetcher;
        this.meFetcher = meFetcher;
        this.endTurnFetcher = endTurnFetcher;
        this.nextQuestionFetcher = nextQuestionFetcher;
        this.answerQuestionFetcher = answerQuestionFetcher;
        this.registerPlayerFetcher = registerPlayerFetcher;
    }

    public DataFetcher<List<Quiz>> getQuizzesFetcher() {
        return dataFetchingEnvironment -> getQuizzesFetcher.get(dataFetchingEnvironment);
    }

    // TODO: DUPE below?
    public DataFetcher<Object> registerPlayerFetcher() {
        return dataFetchingEnvironment -> registerPlayerFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> resetFetcher() {
        return dataFetchingEnvironment -> resetFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<String> login() {
        // TODO - even easier wiring?
        return dataFetchingEnvironment -> loginFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> playerJoinCurrentGameFetcher() {
        return dataFetchingEnvironment -> playerJoinCurrentGameFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> selectGame() {
        return dataFetchingEnvironment -> selectGameFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<GameStatus> getGameStatus() {
        return dataFetchingEnvironment -> gameStatusFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<PanelGameStatus> getPanelGameStatus() {
        return dataFetchingEnvironment -> panelGameStatusFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> beginGamePlay() {
        return dataFetchingEnvironment ->  beginGamePlayFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> endTurn() {
        return dataFetchingEnvironment -> endTurnFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> nextQuestion() {
        return dataFetchingEnvironment -> nextQuestionFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<String> getMe() {
        return dataFetchingEnvironment -> meFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> answerQuestion() {
        return dataFetchingEnvironment -> answerQuestionFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<String> registerPlayer() {
        return dataFetchingEnvironment -> registerPlayerFetcher.get(dataFetchingEnvironment);
    }

    public DataFetcher<Boolean> ping() {
        return dataFetchingEnvironment -> pingFetcher.get(dataFetchingEnvironment);
    }
}
