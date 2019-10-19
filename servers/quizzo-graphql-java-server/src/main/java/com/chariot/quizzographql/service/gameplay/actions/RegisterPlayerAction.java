package com.chariot.quizzographql.service.gameplay.actions;

import com.chariot.quizzographql.models.PlayerScores;
import com.chariot.quizzographql.security.SecurityUtils;
import com.chariot.quizzographql.service.gameplay.GameEvents;
import com.chariot.quizzographql.service.gameplay.GameStates;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

// TODO - rename this to "JoinPlayerToGame" state and actions... Much better since
// register is actually what we do when we set up a new user that is a player...
@Service
public class RegisterPlayerAction implements Action<GameStates, GameEvents> {
    private final static Log logger = LogFactory.getLog(PlayerVoteAction.class);

    @Override
    public void execute(StateContext<GameStates, GameEvents> context) {
        String playerId = (String)context.getMessageHeaders().get("playerId");

        if (playerId == null || playerId.trim().equals("")) {
            logger.debug("No player registered or signed in. Wrong time and should not get here.");
            return;
        }

        PlayerScores playerScores = (PlayerScores) context
                .getExtendedState()
                .getVariables()
                .getOrDefault("playerScores", null);

        if (playerScores != null && !playerScores.isPlayerRegistered(playerId)) {
           playerScores.addPlayer(playerId);
        } else {
           logger.debug("Player already registered or no playerScores created");
        }
    }
}
