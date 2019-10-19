package com.chariot.quizzographql.service;

public interface ModeratorService {
    /**
     * Register a player. Returns join code for user
     * @param nickName
     * @return
     */
    String registerPlayer(String nickName, String email, String phone);

}
