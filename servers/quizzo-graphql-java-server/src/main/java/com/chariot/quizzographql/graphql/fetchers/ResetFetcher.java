package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.service.GamePlayService;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.service.gameplay.QuizMachineFactory;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class ResetFetcher implements DataFetcher<Boolean> {

    private GamePlayService gamePlayService;

    @Autowired
    public ResetFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    public Boolean get(DataFetchingEnvironment environment) throws Exception {
        // TODO - verify what happened with boolean?
        gamePlayService.reset();
        return true;
    }
}
