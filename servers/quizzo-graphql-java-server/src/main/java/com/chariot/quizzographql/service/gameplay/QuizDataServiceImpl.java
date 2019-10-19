package com.chariot.quizzographql.service.gameplay;

import com.chariot.quizzographql.models.PlayerScore;
import com.chariot.quizzographql.models.Quiz;
import com.chariot.quizzographql.models.reporting.FinalPlayerScore;
import com.chariot.quizzographql.repository.PlayerHistory;
import com.chariot.quizzographql.repository.PlayerHistoryRepository;
import com.chariot.quizzographql.repository.QuizDataRepository;
import com.chariot.quizzographql.repository.QuizEntity;
import com.chariot.quizzographql.utils.JsonToQuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;

@Service
public class QuizDataServiceImpl implements QuizDataService {

    private QuizDataRepository quizRepository;

    private PlayerHistoryRepository playerHistoryRepository;

    @Autowired
    public void setQuizRepository(
            QuizDataRepository quizRepository,
            PlayerHistoryRepository playerHistoryRepository) {
        this.quizRepository = quizRepository;
        this.playerHistoryRepository = playerHistoryRepository;
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @Transactional(readOnly = true)
    public Quiz getQuizById(Long quizId) {
        Optional<QuizEntity> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isPresent()) {
            Quiz quiz = JsonToQuizMapper.mapJSONToQuizData(quizOptional.get().getQuizData());
            QuizEntity quizEntity = quizOptional.get();
            quiz.setId(quizEntity.getId());
            quiz.setDescription(quizEntity.getDescription());
            quiz.setTitle(quizEntity.getTitle());
            return quiz;
        } else {
            return null;
        }
    }

    @Override
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @Transactional(readOnly = true)
    public List<Quiz> getQuizzes() {
        Iterable<QuizEntity> quizList = quizRepository.findAll();
        List<Quiz> target = new ArrayList<>();
        Spliterator quizItems = quizList.spliterator();
        quizItems.forEachRemaining(qi -> {
            QuizEntity quizEntity = (QuizEntity)qi;
            Quiz quiz = JsonToQuizMapper.mapJSONToQuizData(quizEntity.getQuizData());
            quiz.setId(((QuizEntity) qi).getId());
            quiz.setDescription(((QuizEntity) qi).getDescription());
            quiz.setTitle(((QuizEntity) qi).getTitle());
            target.add(quiz);
        });
        return target;
    }

    @Override
    @Transactional
    public void savePlayerScore(String quizRunId, Quiz quiz, PlayerScore playerScore) {
        playerHistoryRepository.save(PlayerHistory.from(quiz, quizRunId, playerScore));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAnswerForPlayer(String quizInstanceId, Integer questionId, String playerNickName) {
        // TODO make these params the same?
        return playerHistoryRepository.answerRecordedForPlayerQuestion(playerNickName, quizInstanceId, questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinalPlayerScore> fetchFinalScores(String quizInstanceId) {
        return playerHistoryRepository.finalScoresForPlayers(quizInstanceId);
    }
}
