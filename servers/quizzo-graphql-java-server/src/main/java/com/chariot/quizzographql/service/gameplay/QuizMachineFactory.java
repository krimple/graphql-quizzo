package com.chariot.quizzographql.service.gameplay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizMachineFactory {

    @Autowired
    public QuizMachineFactory(StateMachineFactory<GameStates, GameEvents> factory) {
        this.factory = factory;
    }

    private StateMachineFactory<GameStates, GameEvents> factory;

    private StateMachine<GameStates, GameEvents> stateMachine;

    public void resetStateMachine() {
        if (stateMachine != null && !stateMachine.isComplete()) {
            stateMachine.getExtendedState().getVariables().clear();
        }

        stateMachine = null;
    }

   /**
     * Call to create a new Quiz game.
     *
     * @return
     * @throws Exception
     */
    public void newGameInstance(long quizId) throws Exception {
        stateMachine = this.factory.getStateMachine();
        stateMachine.start();
        Map<String, Object> headers = new HashMap<>();
        headers.put("quizId", quizId);
        stateMachine.sendEvent(MessageBuilder.createMessage(GameEvents.SELECT_GAME, new MessageHeaders(headers)));
    }

    public StateMachine<GameStates, GameEvents> getStateMachine() {
        return stateMachine;
    }
}
