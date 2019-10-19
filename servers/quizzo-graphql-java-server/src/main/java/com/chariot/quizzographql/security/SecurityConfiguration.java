package com.chariot.quizzographql.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;


// DIRECTLY lifted and tweaked from SO post and discussion
// https://stackoverflow.com/questions/45959234/authentication-in-spring-boot-using-graphql
@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Autowired
    private QuizzoUserDetailsService userDetailsService;

    @Autowired
    public LogoutHandler logoutHandler() {
        return new QuizzoLogoutHandler();
    }

    @Autowired
    PasswordEncoder delegatingPasswordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    QuizzoAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(delegatingPasswordEncoder);
        auth.authenticationProvider(authProvider);
    }

    @Autowired
    JwtTokenManager tokenManager;

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() throws Exception {
        return new JwtTokenAuthenticationFilter(authProvider, userDetailsService, tokenManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
           .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
           .cors().and().csrf().disable()
           .headers();
     }

    @Override
    public void configure(final WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/graphql/**");
    }
}