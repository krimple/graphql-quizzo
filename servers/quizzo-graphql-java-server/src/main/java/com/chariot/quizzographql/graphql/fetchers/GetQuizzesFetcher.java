package com.chariot.quizzographql.graphql.fetchers;

import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.service.gameplay.QuizDataService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetQuizzesFetcher implements DataFetcher<List<Quiz>> {
    private final Log logger = LogFactory.getLog(this.getClass());

    private QuizDataService gameDataService;

    @Autowired
    public GetQuizzesFetcher(QuizDataService gameDataService) {
        this.gameDataService = gameDataService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> get(DataFetchingEnvironment environment) throws Exception {
        try {
            List<Quiz> quizzes = gameDataService.getQuizzes();
            if (quizzes == null) {
                throw new RuntimeException("No data found.");
            } else {
                logger.info("Quiz data returned");
                logger.info(quizzes.toString());
                return quizzes;
            }
        } catch (SecurityException se) {
            logger.error("Not secured...", se);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Failed fetching quizzes", e);
            throw new RuntimeException(e);
        }
    }
}

