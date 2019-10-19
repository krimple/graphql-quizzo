package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.service.GamePlayService;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.service.gameplay.QuizMachineFactory;
import com.chariot.quizzographql.security.SecurityUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnswerQuestionFetcher implements DataFetcher<Boolean> {

    private final Log logger = LogFactory.getLog(this.getClass());

    private GamePlayService gamePlayService;

    public AnswerQuestionFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    @Secured({"ROLE_PLAYER", "ROLE_ADMIN", "ROLE_MODERATOR"})
    public Boolean get(DataFetchingEnvironment environment) {
        Map<String, Object> headers = new HashMap<>();
        String answer = environment.getArgument("answer");
        return this.gamePlayService.answerQuestion(SecurityUtils.getCurrentPrincipalName(), answer);
    }
}
