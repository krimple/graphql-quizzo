package com.chariot.quizzographql.service.gameplay;

import com.chariot.quizzographql.models.PlayerScore;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;

import java.util.List;

public interface QuizDataService {
    Quiz getQuizById(Long quizId);
    List<Quiz> getQuizzes();
    void savePlayerScore(String quizRunId, Quiz quiz, PlayerScore playerScore);
    boolean hasAnswerForPlayer(String quizInstanceId, Integer questionId, String playerNickName);
    List<FinalPlayerScore> fetchFinalScores(String quizInstanceId);
}
