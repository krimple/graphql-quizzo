package com.chariot.quizzographql;

import com.chariot.quizzographql.security.QuizzoAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TracerBullet {

    @Autowired
    ApplicationContext context;

    @Autowired
    QuizzoAuthenticationProvider manager;

    @PostConstruct
    public void doSomething() {
        System.out.println("happy days!");
    }
}
