package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.service.GamePlayService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class SelectGameFetcher implements DataFetcher<Boolean> {

    private GamePlayService gamePlayService;

    @Autowired
    public SelectGameFetcher(
            GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @Override
    public Boolean get(DataFetchingEnvironment environment) {
        Integer quizId = environment.getArgument("quizId");
        // TODO - ACK PTHHPTP
        long longQuizId = quizId.longValue();

        return gamePlayService.loadQuizAndBeginRegistration(longQuizId);
    }
}

