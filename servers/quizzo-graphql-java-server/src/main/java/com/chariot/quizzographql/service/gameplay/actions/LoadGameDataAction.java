package com.chariot.quizzographql.service.gameplay.actions;

import com.chariot.quizzographql.models.PlayerScores;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.service.gameplay.QuizDataService;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class LoadGameDataAction implements Action<GameStates, GameEvents> {
    private final static Log logger = LogFactory.getLog(PlayerVoteAction.class);

    private QuizDataService quizDataService;

    @Autowired
    public LoadGameDataAction(QuizDataService quizDataService) {
        this.quizDataService = quizDataService;
    }

    @Override
    public void execute(StateContext<GameStates, GameEvents> context) {
        if (context.getSource().getId().equals(GameStates.IDLE)) {
            Long quizId = (Long) context.getMessageHeader("quizId");

            // TODO - highlight the silliness of Integer -vs- Long mapping in GraphQL -vs- Hibernate/JPA
            Quiz quiz = quizDataService.getQuizById(quizId);

            // guard - if all else fails, bail!
            if (quiz == null) {
                context.getStateMachine().sendEvent(GameEvents.GAME_NOT_FOUND);
                return;
            }

            // otherwise store it
            Map<Object, Object> variables = context.getExtendedState().getVariables();
            variables.put("quiz", quiz);

            // store other game state info
            // TODO - eliminate now that we're using a factory??
            // generate quiz uuid
            UUID uuid = UUID.randomUUID();
            variables.put("quizRunId", uuid.toString());

            // keep track of players and scores
            PlayerScores playerScores = new PlayerScores(quiz.getId());
            variables.put("playerScores", playerScores);
        }
    }
}

