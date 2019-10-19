package com.chariot.quizzographql.security;

import org.apache.catalina.connector.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Second-stage filter for when we have a bearer token in the request
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    public JwtTokenAuthenticationFilter(QuizzoAuthenticationProvider authenticationProvider,
                                        QuizzoUserDetailsService userDetailsService,
                                        JwtTokenManager tokenManager) {
        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
    }

    private QuizzoAuthenticationProvider authenticationProvider;
    private QuizzoUserDetailsService userDetailsService;
    private JwtTokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String jwt = this.getJwtFromRequest(request);

            // inspect JWT token only if exists in request
            if (StringUtils.hasText(jwt)) {
                if (tokenManager.validateToken(jwt)) {
                    // good token, login
                    createUserFromToken(jwt, request);
                    chain.doFilter(request, response);
                } else {
                    // bad or expired token
                    logger.info("Token invalid or has expired.");
                    response.sendError(Response.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                // no token sent, make anonymous and allow /graphql login case to happen
                SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthentication());//prevent show login form...
                chain.doFilter(request, response);
            }
        } catch (Exception ex) {
            // unexpected failure... blow up
            logger.error("Failed JWT Filter", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException("Failed JWT filter.", ex);
        }
    }

    private void createUserFromToken(String jwt, HttpServletRequest request) {
        String userName = tokenManager.getUsername(jwt);

        List<GrantedAuthority> roles = tokenManager.getRoles(jwt).stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        UserDetails userDetails = User.withUsername(userName)
            .authorities(roles)
            .password("NOTHING TO SEE HERE")
            .accountLocked(false)
            .accountExpired(false)
            .disabled(false)
            .build();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, roles));
    }

    // from https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
