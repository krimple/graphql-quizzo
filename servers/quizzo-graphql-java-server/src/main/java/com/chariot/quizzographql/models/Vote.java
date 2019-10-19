package com.chariot.quizzographql.models;

public class Vote {
    int quizId;
    int questionId;
    String questionText;
    boolean answered;
    String answer;
    String points;
}
