package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.service.ModeratorService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegisterPlayerFetcher implements DataFetcher<String> {
    private final Log logger = LogFactory.getLog(this.getClass());

    private ModeratorService moderatorService;

    @Autowired
    public RegisterPlayerFetcher(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public String get(DataFetchingEnvironment environment) throws Exception {
        // TODO - handle player already exists by nickname?
        Map<String, String> newPlayerParameters = environment.getArgument("details");
        String userName = newPlayerParameters.get("nickName");
        String emailAddress = newPlayerParameters.get("email");
        String telephone = newPlayerParameters.get("phone");
        return moderatorService.registerPlayer(userName, emailAddress, telephone);
    }
}
