package com.chariot.quizzographql.service.gameplay.actions;

import com.chariot.quizzographql.models.Question;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AssignNextQuestionAction implements Action<GameStates, GameEvents> {
    private final static Log logger = LogFactory.getLog(PlayerVoteAction.class);

    @Override
    public void execute(StateContext<GameStates, GameEvents> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        Quiz quiz = (Quiz) variables.get("quiz");
        // if we don't have a question index, set it to 0, otherwise increment
        int nextQuestionIndex = (int) variables.getOrDefault("currentQuestionIdx", -1) + 1;
        if (nextQuestionIndex > quiz.getQuestions().size() - 1) {
            variables.put("moreQuestions", false);
        } else {
            variables.put("moreQuestions", true);
            Question question = quiz.getQuestions().get(nextQuestionIndex);
            // TODO: sloppy, pull Id and Idx up together or eliminate Id and use from currentQuestion?
            variables.put("currentQuestion", question);
            variables.put("currentQuestionId", question.getId());
            variables.put("currentQuestionIdx", nextQuestionIndex);
        }

    }
}
