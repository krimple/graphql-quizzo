import * as moderatorActionCreators from '../../../../../store/moderator-action-creators';
import {QuizManager} from '../../../../../quiz-manager';
import logger from '../../../../../server/logger';

const quizManager = QuizManager.instance();
export default function nextTurnMutation(_, payload) {
  quizManager.dispatch(moderatorActionCreators.requestEndTurn());
  const { questions, questionIndex } = quizManager.getState().quiz;
  return questionIndex > questions.length - 1;
}
