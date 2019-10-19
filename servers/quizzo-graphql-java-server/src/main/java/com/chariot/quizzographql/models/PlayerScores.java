package com.chariot.quizzographql.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlayerScores {
    private final Log logger = LogFactory.getLog(this.getClass());

    private Set<String> players = new HashSet<>();

    private long quizId;
    // structure is:
    //  outer scoresByPlayerId collection:
    //  key:  userName
    //  value:  A Map of all player scores by question id
    private Map<Integer, List<PlayerScore>> scoresByQuestionId = new ConcurrentHashMap<>();

    public PlayerScores(long quizId) {
        this.quizId = quizId;
    }

    public List<PlayerScore> getAllScoresForQuestion(int questionId) {
        return this.scoresByQuestionId.get(questionId);
    }

    public void addPlayer(String playerName) {
        if (!players.contains(playerName)) {
            players.add(playerName);
        } else {
            logger.info("Player already registered - " + playerName);
        }
    }

    public boolean isPlayerRegistered(String playerId) {
        return players.contains(playerId);
    }

    public boolean hasScoreForPlayerAndQuestion(String playerId, int questionId) {
        List<PlayerScore> scoresForQuestion = this.scoresByQuestionId.get(questionId);
        return scoresForQuestion == null ? false: scoresForQuestion.stream().anyMatch(score -> score.getPlayerId().equals(playerId));
    }

    public void addScoreForPlayer(String playerId, PlayerScore playerScore) {
        int questionId = playerScore.getQuestionId();
        List<PlayerScore> scoresForQuestion;
        if (!this.scoresByQuestionId.containsKey(questionId)) {
            scoresForQuestion = new ArrayList<>();
        } else {
            scoresForQuestion = this.scoresByQuestionId.get(questionId);
        }

        if (scoresForQuestion.stream().noneMatch(score -> score.getPlayerId().equals(playerId))) {
            playerScore.setPlayerId(playerId);
            scoresForQuestion.add(playerScore);
        } else {
            logger.info("Player " + playerId + " already voted on this question.");
        }
    }

/*    public Map<String, Integer> totalScoreByPlayerId() {
        Map<String, Integer> totalScoresByPlayerId = new HashMap<>();

        for (Map.Entry<String, Map<Integer, PlayerScore>> scoresForPlayer: scoresByPlayerId.entrySet()) {

           int total = 0;

           for (PlayerScore playerScore: scoresForPlayer.getValue().values()) {
               total = total + playerScore.getPoints();
           }
           totalScoresByPlayerId.put(scoresForPlayer.getKey(), total);
        }
        return totalScoresByPlayerId;
    }
 */
}
