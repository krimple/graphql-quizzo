package com.chariot.quizzographql.utils;

import com.chariot.quizzographql.models.Option;
import com.chariot.quizzographql.models.Question;
import com.chariot.quizzographql.models.Quiz;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonToQuizMapper {
    private final static Log logger = LogFactory.getLog(JsonToQuizMapper.class);

    public static Quiz mapJSONToQuizData(String json) throws RuntimeException {

        // TODO - re-usable?
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(json);
            Quiz quiz = new Quiz();
            JsonNode questionNode = root.path("questions");
            List<Question> questions = new ArrayList<>();
            if (questionNode.isMissingNode()) {
                logger.info("no questions for this quiz.");
                quiz.setQuestions(new ArrayList<>());
                return quiz;
            } else {
                for (JsonNode question: questionNode) {
                   Question q = new Question();
                   q.setText(question.path("text").asText());
                   q.setId(question.path("id").asInt());

                   JsonNode optionsNode = question.path("options");
                   List<Option> options = new ArrayList<>();;

                   if (optionsNode.isMissingNode()) {
                       logger.info("no choices available for this question");
                       q.setOptions(options);
                   } else {
                       for (JsonNode option: optionsNode) {
                           Option o = new Option();
                           o.setKey(option.path("key").asText());
                           o.setLabel(option.path("label").asText());
                           o.setScore(option.path("score").asInt());
                           options.add(o);
                       }
                   }

                   q.setOptions(options);
                   questions.add(q);
                }
                quiz.setQuestions(questions);
            }

            return quiz;

        } catch (Exception e) {
            // soften and throw runtime to springize it
            throw new RuntimeException("Unparseable JSON tree.", e);
        }
    }
}
