import { createStore, combineReducers, applyMiddleware } from 'redux';
import createSagaMiddleware from 'redux-saga';
import quizReducer from './quiz/quiz-reducer';
import playersReducer from './players/players-reducer';
import securityReducer from './security/security-reducer';
import thunk from 'redux-thunk';
import {scoringSaga} from './sagas/scoring-saga';
import {gamePlaySaga} from './sagas/moderated-gameplay-saga';
import {GameMode, Quiz, User} from '../graphql/generated/graphql';
import {Player} from './models/player';
import {logger} from 'redux-logger';
// for debugging for now
import { composeWithDevTools } from 'remote-redux-devtools';
import {loadUsers} from './security/security-action-creators';
import {loadQuizData} from './crosscut-action-creators';
import {QuizManager} from '../quiz-manager';

const sagaMiddleware = createSagaMiddleware();

function buildStore() {
  // TODO - externalize config to shut off devtools when
  // in production mode
  const composeEnhancers = composeWithDevTools({ realtime: true, hostname: 'localhost', port:8000 });

  // Build the store with Thunk, Saga Middleware and Logger
  // TODO - zap out logger in production mode?
  const store = createStore(
    combineReducers({
      quiz: quizReducer,
      players: playersReducer,
      security: securityReducer
    }),
    { quiz: { quizzes: [] as Quiz[], security: [] as User[], gameMode: GameMode.Idle }, players: [] as Player[] },
    composeEnhancers(applyMiddleware(sagaMiddleware, thunk, logger))
  );

  // kick off any sagas to run the game
  sagaMiddleware.run(gamePlaySaga);
  sagaMiddleware.run(scoringSaga);

  // TODO - get the typescript signatures right
  // to handle thunk methods in dispatch in TypesScript
  // for now, suppress

  // Load the users and quiz data on startup

  // @ts-ignore
  store.dispatch(loadUsers());
  // @ts-ignore
  store.dispatch(loadQuizData());

  return store;
}

export default buildStore;
