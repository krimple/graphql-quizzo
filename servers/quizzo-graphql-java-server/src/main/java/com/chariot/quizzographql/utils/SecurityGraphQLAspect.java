package com.chariot.quizzographql.utils;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// THANK YOU HUGELY TO https://mi3o.com/spring-graphql-security/

@Aspect
@Component
@Order(1)
public class SecurityGraphQLAspect {

    /**
     * All graphQLResolver methods can be called only by authenticated user.
     * Exclusions are named in Pointcut expression.
     */
    @Before("isDefinedInApplication() && !isUnsecuredResourceLoginFetcher() && !isUnsecuredResourceMethodMeFetcher() && !isUnsecuredResourceMethodPingFetcher()")
    public void doSecurityCheck() {
        if (SecurityContextHolder.getContext() == null ||
                SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
                AnonymousAuthenticationToken.class.isAssignableFrom(SecurityContextHolder.getContext().getAuthentication().getClass())) {
            throw new AccessDeniedException("User not authenticated");
        }
    }
    // ?? apply to the standard GraphQL APIs I suppose
    // @Pointcut("target(com.coxautodev.graphql.tools.GraphQLResolver)")
    // private void allGraphQLResolverMethods() {
    // }
    /**
     * Matches all beans in com.mi3o.springgraphqlsecurity package
     * resolvers must be in this package (subpackages)
     */
    @Pointcut("within(com.chariot.quizzographql.graphql.fetchers.*)")
    private void isDefinedInApplication() {
    }

    /**
     * Exact method signature which will be excluded from security check
     */
    @Pointcut("execution(public String com.chariot.quizzographql.graphql.fetchers.LoginFetcher.get(*))")
    private void isUnsecuredResourceLoginFetcher() {
    }

    @Pointcut("execution(public String com.chariot.quizzographql.graphql.fetchers.MeFetcher.get(*))")
    private void isUnsecuredResourceMethodMeFetcher() {
    }

    @Pointcut("execution(public String com.chariot.quizzographql.graphql.fetchers.PingFetcher.get(*))")
    private void isUnsecuredResourceMethodPingFetcher() {
    }
}
