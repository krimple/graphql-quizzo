package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.models.PlayerScores;
import com.chariot.quizzographql.service.GamePlayService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeginGamePlayFetcher implements DataFetcher<Boolean> {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public BeginGamePlayFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    private GamePlayService gamePlayService;

    @Override
    public Boolean get(DataFetchingEnvironment environment) throws Exception {
       logger.debug("Beginning gameplay");
       return gamePlayService.endRegistrationAndBeginGamePlay();
    }
}
