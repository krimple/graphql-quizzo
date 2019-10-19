package com.chariot.quizzographql.service.gameplay.actions;

import com.chariot.quizzographql.models.*;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import com.chariot.quizzographql.repository.PlayerHistory;
import com.chariot.quizzographql.repository.PlayerHistoryRepository;
import com.chariot.quizzographql.repository.PlayerScoreReportService;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReportVotesAction implements Action<GameStates, GameEvents> {
    private final static Log logger = LogFactory.getLog(ReportVotesAction.class);

    private PlayerScoreReportService reportService;

    @Autowired
    public ReportVotesAction(PlayerScoreReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    @Transactional
    public void execute(StateContext<GameStates, GameEvents> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        String quizRunId = variables.get("quizRunId").toString();
        // TODO - implement in state machine instead of service
        Question currentQuestion = (Question)variables.get("currentQuestion");

        List<PlayerScoreReportEntry> scoresForQuestion =  this.reportService.playerScoresForQuestion(quizRunId, currentQuestion.getId());
        variables.put("scoresForQuestion", scoresForQuestion);
    }
}



