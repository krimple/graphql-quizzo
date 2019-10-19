package com.chariot.quizzographql.repository;

import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.models.reporting.PlayerScoreReportEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PlayerScoreReportServiceJdbcImpl implements PlayerScoreReportService {

    private final Log logger = LogFactory.getLog(this.getClass());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerScoreReportServiceJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerScoreReportEntry> playerScoresForQuestion(String quizRunId, int questionId) {
        List<PlayerScoreReportEntry> reportData = this.jdbcTemplate.query(
                "select player_nickname, answer_score, answer_text, answer_value" +
                        " from quizzo.player_history" +
                        " where quiz_run_id = ? and question_id = ?", new RowMapper<PlayerScoreReportEntry>() {
                    @Override
                    public PlayerScoreReportEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                        PlayerScoreReportEntry entry = new PlayerScoreReportEntry();
                        entry.setAnswerText(rs.getString("answer_text"));
                        entry.setAnswer(rs.getString("answer_value"));
                        entry.setNickName(rs.getString("player_nickname"));
                        entry.setScore(rs.getInt("answer_score"));
                        return entry;
                    }
                }, quizRunId, questionId);
        logger.info("Player score report: " + reportData.toString());
        return reportData;
    }

    public PlayerScoreReportEntry playerScoresForQuestionAndPlayer(String quizRunId, String playerId, int questionId) {
        List<PlayerScoreReportEntry> reportData = this.jdbcTemplate.query(
                "select player_nickname, answer_score, answer_value, answer_text" +
                        " from quizzo.player_history" +
                        " where quiz_run_id = ? and question_id = ?" +
                        " and player_nickname = ?", new RowMapper<PlayerScoreReportEntry>() {
                    @Override
                    public PlayerScoreReportEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                        PlayerScoreReportEntry entry = new PlayerScoreReportEntry();
                        entry.setNickName(rs.getString("player_nickname"));
                        entry.setScore(rs.getInt("answer_score"));
                        entry.setAnswer(rs.getString("answer_value"));
                        entry.setAnswerText(rs.getString("answer_text"));
                        return entry;
                    }
                }, quizRunId, questionId, playerId);

        if (reportData.size() == 0) {
            PlayerScoreReportEntry blankEntry = new PlayerScoreReportEntry();
            blankEntry.setNickName(playerId);
            blankEntry.setScore(0);
            return blankEntry;
        } else if (reportData.size() > 1) {
            logger.warn("Should not allow more than 1 vote per player!");
            // default to last entry
            return reportData.get(reportData.size() - 1);
        } else {
            return reportData.get(0);
        }
    }

    @Override
    public List<FinalPlayerScore> finalScoresForGame(String quizRunId) {
        List<FinalPlayerScore> reportData = this.jdbcTemplate.query(
                "select player_nickname, sum(answer_score) as score" +
                        " from quizzo.player_history" +
                        " where quiz_run_id = ? " +
                        " group by player_nickname", new RowMapper<FinalPlayerScore>() {
                    @Override
                    public FinalPlayerScore mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new FinalPlayerScore(rs.getString("player_nickname"), rs.getInt("score"));
                    }
                }, quizRunId);
        return reportData;
    }
}
