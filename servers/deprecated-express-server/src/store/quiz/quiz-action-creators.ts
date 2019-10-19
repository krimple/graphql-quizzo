import * as actions from './quiz-actions';
import { getQuizList, getQuizData } from '../../db/game-repository';


export function endGame() {
  return {
    type: actions.QUIZ_ACTION_END_GAME
  };
}

export function requestGameStart(quizId: number) {
  return {
    type: actions.QUIZ_ACTION_REQUEST_GAME_START,
    quizId
  };
}

export function acceptAnswer(nickName: string, answer: string) {
  return {
    type: actions.QUIZ_ACTION_ACCEPT_ANSWER,
    nickName,
    answer
  };
}

export function endTurn() {

}


