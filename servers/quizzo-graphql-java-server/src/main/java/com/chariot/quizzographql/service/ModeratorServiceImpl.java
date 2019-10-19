package com.chariot.quizzographql.service;

import com.chariot.quizzographql.repository.PlayerRepository;
import com.chariot.quizzographql.repository.UserEntity;
import com.chariot.quizzographql.service.gameplay.QuizMachineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeratorServiceImpl implements ModeratorService {

    private PlayerRepository playerRepository;

    @Autowired
    public ModeratorServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    /**
     * Returns joincode for player
     */
    public String registerPlayer(String nickName, String email, String phone) {
       return playerRepository.addPlayer(nickName, email, phone);
    }
}
