import * as crossCutActionCreators from '../../../../../store/crosscut-action-creators';
import {QuizManager} from '../../../../../quiz-manager';
import logger from '../../../../../server/logger';

const quizManager = QuizManager.instance();
export default function startGamePlayMutation(_, payload) {
  logger.info('GraphQL mutation requested - moderator starting gameplay');
  quizManager.dispatch(crossCutActionCreators.startGamePlay());
  return true;
}
