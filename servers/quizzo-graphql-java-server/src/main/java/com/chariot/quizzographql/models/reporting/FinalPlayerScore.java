package com.chariot.quizzographql.models.reporting;

public class FinalPlayerScore {
    private String nickName;
    private long score;

    public FinalPlayerScore(String nickName, long score) {
        this.nickName = nickName;
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

