import { QuizManager } from './quiz-manager';
import buildStore from './store/build-store';
import buildServer from './server';
const quizManager = QuizManager.instance();
const data = quizManager.get();
const store = buildStore(data);
// TODO tangled dependencies between quiz management and redux store, fix
quizManager.storeReference = store;

buildServer();
