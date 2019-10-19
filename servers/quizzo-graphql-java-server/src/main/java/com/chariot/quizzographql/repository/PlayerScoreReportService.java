package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;

import java.util.List;

public interface PlayerScoreReportService {

    List<PlayerScoreReportEntry> playerScoresForQuestion(String quizRunId, int questionId);
    PlayerScoreReportEntry playerScoresForQuestionAndPlayer(String quizRunId, String playerId, int questionId);
    List<FinalPlayerScore> finalScoresForGame(String quizRunId);

}
