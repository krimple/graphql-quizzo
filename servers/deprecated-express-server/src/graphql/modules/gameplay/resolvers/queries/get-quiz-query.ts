import store from '../../../../../store/store';

export default function getQuiz(_, args, ctx): any {
    return store.getState().quiz.gameList.find(g => g.quiz_id === args.id);
}
