package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.service.GamePlayService;
import com.chariot.quizzographql.service.gameplay.QuizDataService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndGameFetcher implements DataFetcher<List<FinalPlayerScore>> {

    private GamePlayService gamePlayService;

    @Autowired
    public EndGameFetcher(QuizDataService quizDataService, GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    public List<FinalPlayerScore> get(DataFetchingEnvironment environment) throws Exception {
        return gamePlayService.getFinalScoresForCurrentQuizInstance();
    }
}
