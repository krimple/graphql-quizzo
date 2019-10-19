package com.chariot.quizzographql.repository;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "quiz", schema = "quizzo")
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quizzo.quiz_pk_seq")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Column(length = 1024)
    private String description;

    @Column(name="quizdata", columnDefinition="text")
    private String quizData;

    public QuizEntity() {

    }

    public QuizEntity(Long id, String title, String description, String quizData) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.quizData = quizData;
    }

   public Long getId() {
        return id;
    }

    protected void setId(Long id) {
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
}
