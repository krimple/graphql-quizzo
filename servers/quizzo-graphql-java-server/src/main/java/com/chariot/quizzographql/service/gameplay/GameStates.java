package com.chariot.quizzographql.service.gameplay;

public enum GameStates {
    NOT_RUNNING,                    // psuedo-state that is returned when state machine is not launched
    IDLE,
    AWAITING_PLAYERS,
    PRESENTING_QUESTION,
    EVALUATE_FURTHER_QUESTIONS,
    PRESENTING_SCORES,
    TALLY_FINAL_SCORES,
    GAME_OVER,
    NOT_FOUND
}
