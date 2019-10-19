package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.security.AnonymousAuthentication;
import com.chariot.quizzographql.security.SecurityUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class MeFetcher implements DataFetcher<String> {

    public String get(DataFetchingEnvironment environment) {
        Object o = SecurityContextHolder.getContext().getAuthentication();
        if (o instanceof UsernamePasswordAuthenticationToken) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null) {
                return user.getUsername();
            } else {
                return null;
            }
        } else if (o instanceof AnonymousAuthenticationToken) {
            AnonymousAuthenticationToken anonToken = (AnonymousAuthenticationToken) o;
            return anonToken.getName();
        } else {
            return null;
        }
    }
}
