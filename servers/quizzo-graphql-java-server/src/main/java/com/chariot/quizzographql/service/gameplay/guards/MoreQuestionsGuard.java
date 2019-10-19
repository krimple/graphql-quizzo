package com.chariot.quizzographql.service.gameplay.guards;

import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.service.GamePlayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MoreQuestionsGuard implements Guard<GameStates, GameEvents> {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    // TODO SpEL?
    public boolean evaluate(StateContext<GameStates, GameEvents> context) {
        logger.debug("no more questions left guard");
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        return (boolean)variables.get("moreQuestions");
    }
}
