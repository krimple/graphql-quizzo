package com.chariot.quizzographql.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Question {

    public Question() {

    }

    public Question(String text) {
        this.text = text;
    }

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String text;

    private Character questionType;


    private List<Option> options = new ArrayList<>();


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Character getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Character questionType) {
        this.questionType = questionType;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public PlayerScore scoreAnswerForQuestion(Question question, String answer) {
        // TODO - maybe a better way to choose only one and not force a reduce?
        Optional<Option> choice = options.stream()
                .filter(option -> option.getKey().equals(answer))
                .findFirst();

        PlayerScore seed = new PlayerScore();

        if (choice.isPresent()) {
            Option option = choice.get();
            int score = option.getScore();
            seed.setCorrect(score != 0);
            // TODO - running short on time this weekend, refactor the scoring system to something
            // rational after the conference - for now we set the seed data for the player score (except
            // the player id information, which we won't have here).
            seed.setQuestionText(answer);
            seed.setQuestionId(this.getId());
            seed.setQuestionText(this.getText());
            seed.setAnswer(answer);
            seed.setAnswerText(option.getLabel());
            seed.setCorrect(score != 0);
            seed.setPoints(score);
            return seed;
        } else {
            throw new RuntimeException("No answer assigned with this value: " + answer);
        }
    }

    @Override
    public String toString() {
        return "CurrentQuestion.java{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", options=" + options +
                '}';
    }
}
