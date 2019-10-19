package com.chariot.quizzographql.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_MODERATOR, ROLE_PLAYER;

    public String getAuthority() {
        return name();
    }
}
