import buildStore from './build-store';

const store = buildStore();

export default store;

export function getQuizById(id) {
  return store.getState().quiz.quizList.find(q => q.quiz_id = id);
}

export function findUserById(id) {
  return store.getState().security.find(u => u.id === id);
}
