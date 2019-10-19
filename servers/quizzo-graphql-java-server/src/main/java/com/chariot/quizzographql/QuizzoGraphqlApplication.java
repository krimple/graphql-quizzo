package com.chariot.quizzographql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "com.chariot.quizzographql.repository")
public class QuizzoGraphqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuizzoGraphqlApplication.class, args);
    }
}
