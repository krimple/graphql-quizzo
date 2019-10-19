package com.chariot.quizzographql.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenAuthentication extends AbstractAuthenticationToken {
    private String jwtToken;
    private UserDetails principal;

    public TokenAuthentication(UserDetails principal) {
        super(principal.getAuthorities());
        this.principal = principal;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    public void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }

    public boolean isAuthenticated() {
        return true;
    }
}
