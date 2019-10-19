package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.security.SecurityUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class PingFetcher implements DataFetcher<Boolean> {

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_PLAYER"})
    public Boolean get(DataFetchingEnvironment environment) {
      return SecurityUtils.hasRoles("ROLE_PLAYER", "ROLE_ADMIN", "ROLE_MODERATOR");
    }
}
