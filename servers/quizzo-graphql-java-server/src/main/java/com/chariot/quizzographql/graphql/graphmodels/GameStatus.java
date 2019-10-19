package com.chariot.quizzographql.graphql.graphmodels;

import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;

import java.util.List;

public class GameStatus implements java.io.Serializable {

    public GameStatus(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public CurrentQuestion getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(CurrentQuestion currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public List<ChoiceOption> getCurrentOptions() {
        return currentOptions;
    }

    public void setCurrentOptions(List<ChoiceOption> currentOptions) {
        this.currentOptions = currentOptions;
    }

    public PlayerScoreReportEntry getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(PlayerScoreReportEntry questionScore) {
        this.questionScore = questionScore;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    private String gameMode;
    private String gameTitle;
    private String gameDescription;
    private Integer timeLeft;
    private CurrentQuestion currentQuestion;
    private List<ChoiceOption> currentOptions;
    private PlayerScoreReportEntry questionScore;
    private int finalScore;

    @Override
    public String toString() {
        return "GameStatus{" +
                "gameMode='" + gameMode + '\'' +
                ", gameTitle='" + gameTitle + '\'' +
                ", gameDescription='" + gameDescription + '\'' +
                ", timeLeft=" + timeLeft +
                ", currentQuestion=" + currentQuestion +
                ", currentOptions=" + currentOptions +
                ", questionScore=" + questionScore +
                ", finalScore=" + finalScore +
                '}';
    }
}
