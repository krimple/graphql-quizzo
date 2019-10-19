package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.service.GamePlayService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerJoinCurrentGameFetcher implements DataFetcher<Boolean> {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public PlayerJoinCurrentGameFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    private GamePlayService gamePlayService;

    public Boolean get(DataFetchingEnvironment environment) throws Exception {
        return gamePlayService.registerPlayerForCurrentGame();

    }
}
