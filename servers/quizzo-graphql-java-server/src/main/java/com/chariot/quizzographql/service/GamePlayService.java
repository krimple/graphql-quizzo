package com.chariot.quizzographql.service;

import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import com.chariot.quizzographql.service.gameplay.GameStates;
import com.chariot.quizzographql.models.Question;

import java.util.List;

public interface GamePlayService {
  GameStates getCurrentGameState();
  boolean playerExists(String nickName);
  boolean registerPlayerForCurrentGame();
  boolean answerQuestion(String playerName, String answer);
  boolean isCurrentQuestionAnswered(String playerNickName);
  boolean loadQuizAndBeginRegistration(Long quizId);
  boolean endRegistrationAndBeginGamePlay();
  List<PlayerScoreReportEntry> getScoresForQuestion();
  PlayerScoreReportEntry getPlayerScoreForQuestion();
  List<FinalPlayerScore> generateFinalScoresForCurrentQuizInstance();
  List<FinalPlayerScore> getFinalScoresForCurrentQuizInstance();
  boolean endTurn();
  boolean nextQuestion();
  Quiz getCurrentQuiz();
  Question getCurrentQuestion();
  void reset();
}
