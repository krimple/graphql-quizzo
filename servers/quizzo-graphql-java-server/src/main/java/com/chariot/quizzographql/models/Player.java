package com.chariot.quizzographql.models;

import java.util.Objects;

public class Player {
    private Long id;
    private String nickName;
    private String password;
    private String currentJwtToken;
    private int score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentJwtToken() {
        return currentJwtToken;
    }

    public void setCurrentJwtToken(String currentJwtToken) {
        this.currentJwtToken = currentJwtToken;
    }

    public int getScore() {
        return score;
    }

    /**
     * Add an amount to score, returning the new score
     * @param amount amount to add
     * @return new score for player
     */
    public int addToScore(int amount) {
        this.score = this.score + amount;
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", password='PROTECTED'"  + '\'' +
                ", currentJwtToken='PROTECTED''" +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
