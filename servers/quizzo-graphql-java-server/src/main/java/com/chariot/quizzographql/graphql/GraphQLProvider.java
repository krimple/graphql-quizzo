package com.chariot.quizzographql.graphql;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.execution.SubscriptionExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

// with help from https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/
@Component
public class GraphQLProvider {

    @Autowired
    public GraphQLProvider(GraphQLFrontFetcher graphQLFrontFetcher) {
        this.graphQLFrontFetcher = graphQLFrontFetcher;
    }

    private GraphQL graphQL;
    private GraphQLFrontFetcher graphQLFrontFetcher;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        // Guava - load a resource into an URL
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL
                .newGraphQL(graphQLSchema)
                .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy())
                .build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("getQuizzes", graphQLFrontFetcher.getQuizzesFetcher())
                        .dataFetcher("gameStatus", graphQLFrontFetcher.getGameStatus())
                        .dataFetcher("panelGameStatus", graphQLFrontFetcher.getPanelGameStatus())
                        .dataFetcher("ping", graphQLFrontFetcher.ping())
                        .dataFetcher("me", graphQLFrontFetcher.getMe())
                )
                .type(newTypeWiring("Mutation")
                        // TODO : inconsistencies here in exposing method names - some leave off Fetcher
                        .dataFetcher("registerPlayer", graphQLFrontFetcher.registerPlayerFetcher())
                        .dataFetcher("login", graphQLFrontFetcher.login())
                        .dataFetcher("selectGame", graphQLFrontFetcher.selectGame())
                        .dataFetcher("beginGamePlay", graphQLFrontFetcher.beginGamePlay())
                        .dataFetcher("answerQuestion", graphQLFrontFetcher.answerQuestion())
                        .dataFetcher("endTurn", graphQLFrontFetcher.endTurn())
                        .dataFetcher("nextQuestion", graphQLFrontFetcher.nextQuestion())
                        .dataFetcher("playerJoinCurrentGame", graphQLFrontFetcher.playerJoinCurrentGameFetcher())
                        .dataFetcher("reset", graphQLFrontFetcher.resetFetcher())
                )
                .build();

    }
}