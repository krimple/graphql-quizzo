import * as playerActionCreators from '../../../../../store/players/players-action-creators';
import {GameContext} from '../../../../gameplay-context';
import store from '../../../../../store/store';
import logger from '../../../../../server/logger';

export default function answerQuestion(_, payload, context) {
  logger.info(`User: ${JSON.stringify(context.user)}`);

  const user = context.user;
  // TODO - dispatch answer from player
  store.dispatch(playerActionCreators.submitQuestionForPlayer(user.nickName, payload.input.answer));
  return true;
}
