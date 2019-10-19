package com.chariot.quizzographql.service.gameplay.actions;

import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.repository.PlayerHistoryRepository;
import com.chariot.quizzographql.service.GamePlayService;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportFinalScoresAction implements Action<GameStates, GameEvents> {

    private final static Log logger = LogFactory.getLog(ReportFinalScoresAction.class);

    private PlayerHistoryRepository repository;

    @Autowired
    public ReportFinalScoresAction(PlayerHistoryRepository playerHistoryRepository) {
        this.repository = playerHistoryRepository;
    }

    @Override
    public void execute(StateContext<GameStates, GameEvents> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        String quizRunId = variables.get("quizRunId").toString();
        List<FinalPlayerScore> finalScores = repository.finalScoresForPlayers(quizRunId);
        variables.put("finalScores", finalScores);
        boolean endGameEvent = context.getStateMachine().sendEvent(GameEvents.END_GAME);
        logger.info("End game event success? " + endGameEvent);
    }
}
