import * as actions from './cross-cut-actions';
import {getQuizData} from '../db/game-repository';
import logger from '../server/logger';

export function loadQuizData() {
  return async dispatch => {
    try {
      const quizList = await getQuizData();
      dispatch({
        type: actions.CROSSCUT_ACTION_LOAD_GAME_LIST,
        quizList: quizList
      });
    } catch (e) {
      logger.error(e);
      throw e;
    }
  }
}

export function startRegistration(quizId: number) {
  return {
    type: actions.CROSSCUT_START_REGISTRATION,
    quizId
  };
}

export function startGamePlay() {
  return {
    type: actions.CROSSCUT_START_GAMEPLAY
  };
}

