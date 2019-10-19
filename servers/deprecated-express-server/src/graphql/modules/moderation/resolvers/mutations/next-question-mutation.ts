import * as moderatorActionCreators from '../../../../../store/moderator-action-creators';
import {QuizManager} from '../../../../../quiz-manager';
import logger from '../../../../../server/logger';

const quizManager = QuizManager.instance();
export default function nextQuestionMutation(_, payload) {
  quizManager.dispatch(moderatorActionCreators.requestNextQuestion());
  return true;
}
