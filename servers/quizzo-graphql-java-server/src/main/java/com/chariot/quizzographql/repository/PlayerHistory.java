package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.PlayerScore;
import com.chariot.quizzographql.models.PlayerVote;
import com.chariot.quizzographql.models.Quiz;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "player_history", schema = "quizzo")
public class PlayerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quizzo.player_history_seq")
    private Long id;

    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name="quizRunId", columnDefinition="text")
    private String quizInstanceId;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "S-")
    @Column(name = "answer_date")
    @NotNull
    private Date answerDate;

    @Column(name = "quiz_name")
    @NotNull
    private String quizName;

    @Column(name = "player_nickname")
    @NotNull
    private String playerNickName;

    @Column(name = "question_text")
    @NotNull
    private String questionText;

    @Column(name = "answer_value")
    private String answerValue;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "answer_score")
    @NotNull
    private int answerScore;

    @Column(name = "total_score")
    private int totalScore;

    @Column(name = "final")
    private int isFinalAnswer;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public Date getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getPlayerNickName() {
        return playerNickName;
    }

    public void setPlayerNickName(String playerNickName) {
        this.playerNickName = playerNickName;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public int getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(int answerScore) {
        this.answerScore = answerScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getIsFinalAnswer() {
        return isFinalAnswer;
    }

    public void setIsFinalAnswer(int isFinalAnswer) {
        this.isFinalAnswer = isFinalAnswer;
    }

    public String getQuizInstanceId() {
        return quizInstanceId;
    }

    public void setQuizInstanceId(String quizInstanceId) {
        this.quizInstanceId = quizInstanceId;
    }

    @Override
    public String toString() {
        return "PlayerHistory{" +
                "id=" + id +
                ", answerDate=" + answerDate +
                ", quizName='" + quizName + '\'' +
                ", playerNickName='" + playerNickName + '\'' +
                ", questionText='" + questionText + '\'' +
                ", answerValue='" + answerValue + '\'' +
                ", answerText='" + answerText + '\'' +
                ", answerScore=" + answerScore +
                ", totalScore=" + totalScore +
                ", quizInstanceId=" + quizInstanceId +
                ", isFinalAnswer=" + isFinalAnswer +
                '}';
    }

    public static PlayerHistory from(Quiz quiz, String quizInstanceId, PlayerScore playerScore) {
        PlayerHistory history = new PlayerHistory();
        history.setAnswerDate(new Date());
        history.setAnswerScore(playerScore.getPoints());
        history.setAnswerText(playerScore.getAnswerText());
        history.setAnswerValue(playerScore.getAnswer());
        // TODO - WTH remove this and aggregate on score total display and query
        history.setIsFinalAnswer(0);

        history.setPlayerNickName(playerScore.getPlayerId());
        history.setQuestionText(playerScore.getQuestionText());
        history.setQuestionId(playerScore.getQuestionId());
        history.setQuizInstanceId(quizInstanceId);
        history.setQuizName(quiz.getTitle());
        history.setQuizId(quiz.getId());
        // TODO - aggregate and show this but not in this release - we can derive from reports
        history.setTotalScore(0);
        return history;
    }

    public static PlayerHistory fromPlayerVote(PlayerVote playerVote) {
        PlayerHistory history = new PlayerHistory();
        history.setAnswerDate(new Date());
        history.setAnswerScore(playerVote.getScore());
        history.setAnswerText(playerVote.getAnswerText());
        history.setAnswerValue(playerVote.getAnswerKey());
        // TODO - eliminate or set correctly
        history.setIsFinalAnswer(0);
        history.setPlayerNickName(playerVote.getNickName());
        history.setQuestionText(playerVote.getQuestionTitle());
        history.setQuestionId(playerVote.getQuestionId());
        history.setQuizName(playerVote.getQuizTitle());
        // TODO - eliminate or set correctly
        history.setTotalScore(0);
        history.setQuizInstanceId(playerVote.getQuizInstance());
        return history;
    }
}
