import store from '../../../../../store/store';

export default function getCurrentGameState(_, args, ctx): any {
  const state = store.getState();
  return {
    quizId: state.quiz.quizId,
    gameMode: state.quiz.gameMode
  };
}
