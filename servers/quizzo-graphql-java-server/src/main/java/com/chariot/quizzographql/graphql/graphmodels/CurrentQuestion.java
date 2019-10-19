package com.chariot.quizzographql.graphql.graphmodels;

import java.util.List;

public class CurrentQuestion {
    private String text;
    private List<ChoiceOption> choiceOptions;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ChoiceOption> getChoiceOptions() {
        return choiceOptions;
    }

    public void setChoiceOptions(List<ChoiceOption> choiceOptions) {
        this.choiceOptions = choiceOptions;
    }

    @Override
    public String toString() {
        return "CurrentQuestion{" +
                "text='" + text + '\'' +
                ", choiceOptions=" + choiceOptions +
                '}';
    }
}
