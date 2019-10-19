package com.chariot.quizzographql.service.gameplay;

import com.chariot.quizzographql.service.gameplay.actions.*;
import com.chariot.quizzographql.service.gameplay.guards.MoreQuestionsGuard;
import com.chariot.quizzographql.service.gameplay.guards.NoMoreQuestionsGuard;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.*;
import org.springframework.statemachine.config.builders.*;

@Configuration
@EnableStateMachineFactory
public class GamestateConfiguration extends EnumStateMachineConfigurerAdapter<GameStates, GameEvents> {

    private final Log logger = LogFactory.getLog(this.getClass());
    @Override
    public void configure(StateMachineConfigurationConfigurer<GameStates, GameEvents> config) throws Exception {
        config
                .withConfiguration()
                //.taskExecutor(new SyncTaskExecutor())
                .machineId("QUIZZO_MACHINE")
                .listener(logListener)
                .autoStartup(false);
    }

    @Override
    public void configure(StateMachineStateConfigurer<GameStates, GameEvents> states) throws Exception {
        states.withStates()
                .initial(GameStates.IDLE)
                .end(GameStates.GAME_OVER)
                .end(GameStates.NOT_FOUND)
                .state(GameStates.IDLE)
                .state(GameStates.AWAITING_PLAYERS)
                .state(GameStates.PRESENTING_QUESTION)
                .junction(GameStates.EVALUATE_FURTHER_QUESTIONS)
                .state(GameStates.PRESENTING_SCORES)
                .state(GameStates.TALLY_FINAL_SCORES)
                .state(GameStates.GAME_OVER);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<GameStates, GameEvents> transitions) throws Exception {
       transitions
               // try to find the game and transition to the player await state
               .withExternal()
               // select the next game
               .source(GameStates.IDLE)
               .action(loadGameDataAction)
               .target(GameStates.AWAITING_PLAYERS)
               .event(GameEvents.SELECT_GAME)
               .and()

               // TODO - test this. If we don't find the game, bail on the state machine
               .withExternal()
               .source(GameStates.IDLE)
               .event(GameEvents.GAME_NOT_FOUND)
               .target(GameStates.NOT_FOUND)
               .and()

               // register a player - probably an internal action
               .withInternal()
               .source(GameStates.AWAITING_PLAYERS)
               // TODO: Should be Register player for game rather than just registerPlayer
               // the registering of a player is outside of the gameplay state engine.
               // once logged in, they can join the next game or any other they happen upon
               .event(GameEvents.REGISTER_PLAYER)
               .action(registerPlayerAction)
               .and()

               .withExternal()
               .source(GameStates.AWAITING_PLAYERS)
               .target(GameStates.EVALUATE_FURTHER_QUESTIONS)
               .event(GameEvents.BEGIN_GAMEPLAY)
               .action(assignNextQuestionAction)
               .and()

               .withJunction()
               .source(GameStates.EVALUATE_FURTHER_QUESTIONS)
               .first(GameStates.PRESENTING_QUESTION, moreQuestionsGuard)
               .then(GameStates.TALLY_FINAL_SCORES, noMoreQuestionsGuard)
               .and()

               .withInternal()
               .source(GameStates.PRESENTING_QUESTION)
               .event(GameEvents.ANSWER_QUESTION)
               .action(playerVoteAction)
               .and()

               .withInternal()
               .source(GameStates.PRESENTING_QUESTION)
               .action(endTurnTimedAction()).timerOnce(10000)
               .and()

               .withExternal()
               .source(GameStates.PRESENTING_QUESTION)
               .action(reportVotesAction)
               .target(GameStates.PRESENTING_SCORES)
               .event(GameEvents.END_TURN)
               .and()

               .withInternal()
               .source(GameStates.PRESENTING_SCORES)
               .action(nextQuestionTimedAction()).timerOnce(10000)
               .and()

               .withExternal()
               .source(GameStates.PRESENTING_SCORES)
               .action(assignNextQuestionAction)
               .target(GameStates.EVALUATE_FURTHER_QUESTIONS)
               .event(GameEvents.NEXT_QUESTION)
               .and()

               .withExternal()
               .source(GameStates.TALLY_FINAL_SCORES)
               .action(reportFinalScoresAction)
               .target(GameStates.GAME_OVER)
               .and()

               .withExternal()
               // can leave game while presenting scores
               .source(GameStates.PRESENTING_QUESTION)
               .event(GameEvents.RESET)
               .target(GameStates.GAME_OVER)
               .and()

               .withExternal()
               // can leave game while presenting scores
               .source(GameStates.PRESENTING_SCORES)
               .event(GameEvents.RESET)
               .target(GameStates.GAME_OVER);
    }

    @Bean
    public Action<GameStates, GameEvents> endTurnTimedAction() {
        return context -> {
            logger.debug("************ Question timeout. moving on...");
            context.getStateMachine().sendEvent(GameEvents.END_TURN);
        };
    }

    @Bean
    public Action<GameStates, GameEvents> nextQuestionTimedAction() {
        return context -> {
            logger.debug("************ Question timeout. moving on...");
            context.getStateMachine().sendEvent(GameEvents.NEXT_QUESTION);
        };
    }

    @Autowired
    public GamestateConfiguration(
            StateMachineLogListener logListener,
            PlayerVoteAction playerVoteAction,
            ReportVotesAction reportVotesAction,
            RegisterPlayerAction registerPlayerAction,
            AssignNextQuestionAction assignNextQuestionAction,
            ReportFinalScoresAction reportFinalScoresAction,
            MoreQuestionsGuard moreQuestionsGuard,
            NoMoreQuestionsGuard noMoreQuestionsGuard,
            LoadGameDataAction loadGameDataAction) {
        this.logListener = logListener;
        this.playerVoteAction = playerVoteAction;
        this.reportVotesAction = reportVotesAction;
        this.registerPlayerAction = registerPlayerAction;
        this.reportFinalScoresAction = reportFinalScoresAction;
        this.assignNextQuestionAction = assignNextQuestionAction;
        this.moreQuestionsGuard = moreQuestionsGuard;
        this.noMoreQuestionsGuard = noMoreQuestionsGuard;
        this.loadGameDataAction = loadGameDataAction;
    }

    StateMachineLogListener logListener;

    PlayerVoteAction playerVoteAction;

    ReportVotesAction reportVotesAction;

    RegisterPlayerAction registerPlayerAction;

    ReportFinalScoresAction reportFinalScoresAction;

    AssignNextQuestionAction assignNextQuestionAction;

    MoreQuestionsGuard moreQuestionsGuard;

    NoMoreQuestionsGuard noMoreQuestionsGuard;


    LoadGameDataAction loadGameDataAction;
}
