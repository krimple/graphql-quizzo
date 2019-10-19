package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.PlayerScore;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chariot.quizzographql.models.PlayerVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Repository
public class PlayerHistoryJpaRepository implements PlayerHistoryRepository {

    private final Log logger = LogFactory.getLog(this.getClass());

    private EntityManager entityManager;

    @Autowired
    public PlayerHistoryJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean answerRecordedForPlayerQuestion(String playerName, String quizInstanceId, Integer questionId) {
        List<PlayerHistory> results = entityManager
                .createQuery("select p from PlayerHistory p where p.quizInstanceId = :quizInstanceId" +
                        " and p.questionId = :questionId" +
                        " and p.playerNickName = :playerName", PlayerHistory.class)
                .setParameter("quizInstanceId", quizInstanceId)
                .setParameter("questionId", questionId)
                .setParameter("playerName", playerName)
                .getResultList();
        return (results != null && results.size() > 0);
    }

    @Override
    public List<PlayerScoreReportEntry> questionScoresForPlayers(String quizInstanceId, Integer questionId) {
        List<PlayerScoreReportEntry> results = entityManager
                .createQuery("select new com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry(" +
                        " p.playerNickName,  p.answerText, p.answerValue, p.answerScore)" +
                        " from PlayerHistory p" +
                        " where p.quizInstanceId = :quizInstanceId" +
                        " and p.questionId = :questionId" +
                        " group by p.playerNickName", PlayerScoreReportEntry.class)
                .setParameter("quizInstanceId", quizInstanceId)
                .setParameter("questionId", questionId)
                .getResultList();
        return results;
    }

    @Override
    public List<FinalPlayerScore> finalScoresForPlayers(String quizInstanceId) {
        List<FinalPlayerScore> results = entityManager
                .createQuery("select new com.chariot.quizzographql.models.reporting.FinalPlayerScore(" +
                        " p.playerNickName, sum(p.answerScore))" +
                        " from PlayerHistory p where p.quizInstanceId = :quizInstanceId" +
                        " group by p.playerNickName", FinalPlayerScore.class)
                .setParameter("quizInstanceId", quizInstanceId)
                .getResultList();
        return results;
    }

    @Override
    public List<PlayerHistory> findByQuizName(String quizName) {
        return entityManager
                .createQuery("select p from PlayerHistory p where p.quizName = :quizName", PlayerHistory.class)
                .setParameter("quizName", quizName).getResultList();
    }

    public void save(PlayerHistory playerHistory) {
        entityManager.persist(playerHistory);
    }
}
