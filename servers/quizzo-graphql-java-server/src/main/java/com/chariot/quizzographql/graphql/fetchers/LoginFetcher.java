package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.security.AnonymousAuthentication;
import com.chariot.quizzographql.security.JwtTokenManager;
import com.chariot.quizzographql.security.QuizzoAuthenticationProvider;
import com.chariot.quizzographql.security.QuizzoUserDetailsService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginFetcher implements DataFetcher<String> {
    private QuizzoUserDetailsService userDetailsService;
    private QuizzoAuthenticationProvider authenticationProvider;
    private JwtTokenManager tokenManager;

    @Autowired
    public LoginFetcher(QuizzoUserDetailsService userDetailsService, QuizzoAuthenticationProvider authenticationProvider, JwtTokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.tokenManager = tokenManager;
    }

    @Override
    public String get(DataFetchingEnvironment environment) throws Exception {
        Map<String, String> loginParameters = environment.getArgument("credentials");
        String userName = loginParameters.get("userName");
        String password = loginParameters.get("password");
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        try {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userName, password, userDetails.getAuthorities());
            Authentication auth = authenticationProvider.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return tokenManager.createToken(userName, auth.getAuthorities());
        } catch (AuthenticationException ae) {
            SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthentication());
            ae.printStackTrace();
            return "";
        }
    }
}
