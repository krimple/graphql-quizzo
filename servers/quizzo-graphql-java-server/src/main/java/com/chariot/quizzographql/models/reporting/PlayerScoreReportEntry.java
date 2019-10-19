package com.chariot.quizzographql.models.reporting;

public class PlayerScoreReportEntry {
    private String gameTitle;
    private String questionTitle;
    private String answerText;
    private String answer;
    private String nickName;
    private int score;

    public PlayerScoreReportEntry() {

    }

    public PlayerScoreReportEntry(String nickName, String answerText, String answer, int score) {
        this();
        this.nickName = nickName;
        this.answerText = answerText;
        this.answer = answer;
        this.score = score;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "PlayerScoreReportEntry {" +
                "gameTitle='" + gameTitle + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", answerText='" + answerText + '\'' +
                ", answer='" + answer + '\'' +
                ", nickName='" + nickName + '\'' +
                ", score=" + score +
                '}';
    }
}
