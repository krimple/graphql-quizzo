import * as crossCutActionCreators from '../../../../../store/crosscut-action-creators';
import {QuizManager} from '../../../../../quiz-manager';
import logger from '../../../../../server/logger';

const quizManager = QuizManager.instance();
export default function startRegistrationsMutation(_, payload) {
  logger.info(`moderator mutation - starting registrations for quiz - ${payload.quizId}`);
  quizManager.dispatch(crossCutActionCreators.startRegistration(payload.quizId));
  return true;
}
