package com.chariot.quizzographql.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;

public class SecurityUtils {

    public static boolean hasRoles(String ...roleNames) {
        boolean found = false;
        if (SecurityContextHolder.getContext() != null &&
            SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if (Arrays.stream(roleNames).anyMatch(authority.getAuthority()::equals)) {
                    found = true;
                    break;
                }
            }

        }
        return found;
    }

    public static String getCurrentPrincipalName() {
        if (SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        } else {
            // TODO: handle this properly
            return "";
        }
    }
}
