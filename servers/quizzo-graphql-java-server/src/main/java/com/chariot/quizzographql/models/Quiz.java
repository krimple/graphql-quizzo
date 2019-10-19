package com.chariot.quizzographql.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private Long id;

    private String title;

    private String description;

    private String quizData;

    private List<Question> questions = new ArrayList<>();

    public Quiz() {

    }

    public Quiz(Long id, String title, String description, String quizData) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.quizData = quizData;
    }

   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuizData() {
        return this.quizData;
    }

    public void setQuizData(String quizData) {
        this.quizData = quizData;
    }

    @JsonGetter
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", quizData='" + quizData + '\'' +
                ", questions=" + questions +
                '}';
    }
}
