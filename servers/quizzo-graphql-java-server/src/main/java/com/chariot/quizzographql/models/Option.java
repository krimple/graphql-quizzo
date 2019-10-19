package com.chariot.quizzographql.models;

public class Option {
    private String label;

    private String key;

    private int score;

    public Option() {

    }

    public Option(String label, String key, int score) {
        this.label = label;
        this.key = key;
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Option{" +
                "label='" + label + '\'' +
                ", key='" + key + '\'' +
                ", score=" + score +
                '}';
    }
}
