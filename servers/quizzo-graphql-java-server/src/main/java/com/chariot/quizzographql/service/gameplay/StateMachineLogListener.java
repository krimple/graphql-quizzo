package com.chariot.quizzographql.service.gameplay;

import com.chariot.quizzographql.service.GamePlayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;

@Service
public class StateMachineLogListener extends StateMachineListenerAdapter<GameStates, GameEvents> {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void stateChanged(State<GameStates, GameEvents> from, State<GameStates, GameEvents> to) {
        // log the state to the state engine
        logger.debug("Switched from " + (from != null ? from.getId().name() : "<begin>") + " to " + to.getId().name());
    }

    @Override
    public void stateMachineError(StateMachine<GameStates, GameEvents> stateMachine, Exception exception) {
        logger.error("State machine error!", exception);
    }

    @Override
    public void stateEntered(State<GameStates, GameEvents> state) {
        //logger.debug("entered the state " + state.getId().name());
    }

    @Override
    public void stateExited(State<GameStates, GameEvents> state) {
        //logger.debug("exited the state " + state.getId().name());
    }

    @Override
    public void eventNotAccepted(Message<GameEvents> event) {
        logger.debug("Event not accepted");
        logger.debug(event.getPayload().toString());
        logger.debug("headers");
        logger.debug(event.getHeaders().toString());
    }
}
