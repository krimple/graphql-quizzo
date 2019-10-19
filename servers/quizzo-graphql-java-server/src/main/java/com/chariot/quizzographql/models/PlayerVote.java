package com.chariot.quizzographql.models;

import java.util.List;
import java.util.Optional;

// TODO - Refactor this back to normalcy and synthesize the data when writing to the db
public class PlayerVote {
   private String nickName;
   private Integer questionId;
   private String questionTitle;
   private String answerKey;
   private String answerText;
   private String quizTitle;
   private String quizInstance;
   private Long quizId;
   private Integer score;

   public PlayerVote(String nickName,
                     String answerKey,
                     String answerText,
                     String quizInstance,
                     Quiz quiz,
                     Question question) {
      this.nickName = nickName;
      this.answerKey = answerKey;
      this.answerText = answerText;
      this.setQuizInstance(quizInstance);
      this.setQuizId(quiz.getId());
      this.setQuizTitle(quiz.getTitle());
      this.setQuestionTitle(question.getText());
      this.setQuestionId(question.getId());
      this.questionTitle = question.getText();
      // now score the answer
      // TODO this is rather horrid, quickly coded, denormalized... Could use refactoring into better model
      computeScoreAndAnswerDetails(question.getOptions());
   }

   public Integer getScore() {
      return score;
   }

   public void setScore(Integer score) {
      this.score = score;
   }

   public String getNickName() {
      return nickName;
   }

   public String getAnswerKey() {
      return answerKey;
   }

   public String getQuestionTitle() {
      return questionTitle;
   }

   public String getAnswerText() {
      return answerText;
   }

   public String getQuizTitle() {
      return quizTitle;
   }

   public String getQuizInstance() {
      return quizInstance;
   }

   public Long getQuizId() {
      return quizId;
   }

   public void setNickName(String nickName) {
      this.nickName = nickName;
   }

   public void setQuestionTitle(String questionTitle) {
      this.questionTitle = questionTitle;
   }

   public void setAnswerKey(String answerKey) {
      this.answerKey = answerKey;
   }

   public void setAnswerText(String answerText) {
      this.answerText = answerText;
   }

   public void setQuizTitle(String quizTitle) {
      this.quizTitle = quizTitle;
   }

   public void setQuizInstance(String quizInstance) {
      this.quizInstance = quizInstance;
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

   private void computeScoreAndAnswerDetails(List<Option> choices) {
       Optional<Option> matchingOption = choices.stream().filter(choice -> choice.getKey().equals(this.answerKey)).findFirst();
       if (matchingOption.isPresent()) {
          setAnswerText(matchingOption.get().getLabel());
          setScore(matchingOption.get().getScore());
       } else {
          setScore(0);
       }
   }
}
