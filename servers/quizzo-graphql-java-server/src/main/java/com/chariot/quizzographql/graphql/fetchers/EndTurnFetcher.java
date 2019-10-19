package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import com.chariot.quizzographql.repository.PlayerScoreReportService;
import com.chariot.quizzographql.service.GamePlayService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndTurnFetcher implements DataFetcher<Boolean> {
    private final Log logger = LogFactory.getLog(this.getClass());

    private GamePlayService gamePlayService;


    @Autowired
    public EndTurnFetcher(GamePlayService gamePlayService) {
        this.gamePlayService = gamePlayService;
    }

    @Override
    public Boolean get(DataFetchingEnvironment environment) {
        return gamePlayService.endTurn();
    }
}
