import * as quizActionCreators from '../../../../../store/quiz/quiz-action-creators';
import store from '../../../../../store/store';

export default function startGameMutation(_, payload) {
  store.dispatch(quizActionCreators.requestGameStart(payload.quizId));
  return true;
}
