import { take, select, put } from 'redux-saga/effects';
import * as playerActions from '../players/players-actions';
import * as playerActionCreators from '../players/players-action-creators';
import { pubsub } from '../../config/pubsub-holder';
import logger from '../../server/logger';

export function* scoringSaga() {
  while (true) {
    const action = yield take(playerActions.PLAYER_ACTION_ASSIGN_ANSWER_FOR_QUESTION);
    const state = yield select();
    if (state && state.quiz && state.quiz.question) {
      const question = state.quiz.question;
      const playerAnswer = action.answer;
      const nickName = action.nickName;
      logger.info(`Scoring - ${question} ${playerAnswer} for ${nickName}`);
      // TODO - have different scoring strategies for fill in blank, m/c and others
      // FOR NOW WE JUST ANSWER MC
      if (question.options) {
        const candidateAnswer = question.options.filter((option) => option.key === playerAnswer);
        if (candidateAnswer && candidateAnswer.length === 1) {
          const score = candidateAnswer[0].score;
          yield put(
              playerActionCreators.scoreCurrentPlayerQuestion(nickName, score));
        } else {
          logger.info('Player answer did not match a choice.');
        }
      } else {
        logger.info(`cannot answer question type yet...`);
      }
    }
  }
}
