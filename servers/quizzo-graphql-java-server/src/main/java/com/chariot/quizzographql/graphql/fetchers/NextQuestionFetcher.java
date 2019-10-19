package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.service.GamePlayService;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.service.gameplay.QuizMachineFactory;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class NextQuestionFetcher implements DataFetcher<Boolean> {
    private final Log logger = LogFactory.getLog(this.getClass());

    private GamePlayService gamePlayService;

    @Autowired
    public NextQuestionFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    public Boolean get(DataFetchingEnvironment environment) throws Exception {
        return gamePlayService.nextQuestion();
    }
}
