import store from '../../../../../store/store';

export default function getQuizzes(_, { }): any[] {
    return store.getState().quiz.gameList.map((g) => ({
      quizId: g.quiz_id,
      description: g.description,
      title: g.title,
      numQuestions: g.questions.length
    }));
}
