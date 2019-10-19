package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;

import java.util.List;

public interface PlayerHistoryRepository {
    public boolean answerRecordedForPlayerQuestion(String playerName, String quizInstanceId, Integer questionId);
    public List<FinalPlayerScore> finalScoresForPlayers(String quizInstanceId);
    public List<PlayerScoreReportEntry> questionScoresForPlayers(String quizInstanceId, Integer questionId);
    public abstract List<PlayerHistory> findByQuizName(String quizName);
    public void save(PlayerHistory playerHistory);
}
