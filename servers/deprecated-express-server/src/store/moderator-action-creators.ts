import * as quizActions from './quiz/quiz-actions';
import * as moderatorActions from './moderator-actions';

export function requestEndTurn() {
  return {
    type: moderatorActions.REQUEST_END_TURN
  };
}

export function requestNextQuestion() {
  return {
    type:  quizActions.QUIZ_ACTION_NEXT_QUESTION
  }
}

export function requestStartGame(quizId) {
  return {
    type: quizActions.QUIZ_ACTION_REQUEST_GAME_START,
    quizId
  };
}
