package com.chariot.quizzographql.graphql.graphmodels;

public class ChoiceOption {

    public ChoiceOption(String key, String label) {
        this.key = key;
        this.label = label;
    }

    private String key;
    private String label;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ChoiceOption{" +
                "key='" + key + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
