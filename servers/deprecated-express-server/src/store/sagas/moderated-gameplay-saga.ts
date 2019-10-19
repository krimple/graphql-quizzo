import { take, takeEvery, delay, put, call, select } from 'redux-saga/effects';
import * as moderatorActions from '../moderator-actions';
import * as quizActions from '../quiz/quiz-actions';
import * as playerActions from '../players/players-actions';
import * as crosscutActions from '../cross-cut-actions';
import * as crosscutActionCreators from '../crosscut-action-creators';
import { pubsub } from '../../config/pubsub-holder';
import logger from '../../server/logger';
import {GameState} from '../../graphql/generated/graphql';
import {getQuizById} from '../store';

/** Avoids the while true endless loop - cancels any prior saga */
export function* gamePlaySaga() {
  return yield takeEvery(crosscutActions.CROSSCUT_START_REGISTRATION, beginGame);
}

function* beginGame(payload) {
  console.log(`**** STARTING REGISTRATIONS for ${JSON.stringify(payload)}`);
  const quiz = getQuizById(payload.quizId);
  if (quiz) {
    // logger.info(`using quiz: ${JSON.stringify(quiz, undefined, 2)}`);
    yield put(crosscutActionCreators.startRegistration(quiz));
    yield call(gameSaga, quiz);
  }
}

export function* gameSaga(quiz) {
  // a slight pause while we wait for the moderator to begin the game
  // this is when registrations will happen!
  yield take(crosscutActions.CROSSCUT_START_GAMEPLAY);

  console.log(`*** BEGINNING GAMEPLAY`);
  const questions = quiz.questions;

  // TODO - refactor - naieve implementation
  const gameStartState = yield select();
  const nickNames = gameStartState.players.map((p) => p.nickName);
  yield call(sendStartGameMessage, quiz.id);

  for (const question of questions) {
    yield call(sendNextQuestionMessage, question);
    yield put({
      type: quizActions.QUIZ_ACTION_NEXT_QUESTION,
          question
    });
    yield call(turnSaga, nickNames);
  }
  const finalState = yield select();
  logger.info(`Final player data ${JSON.stringify(finalState.players)}`);
  yield call(sendEndGameMessage, finalState.players);
  yield put({ type: quizActions.QUIZ_ACTION_END_GAME });
  yield put({ type: playerActions.PLAYER_ACTION_END_GAME});
}

export function* turnSaga(nickNames) {
  yield take(moderatorActions.REQUEST_END_TURN);
  logger.info(`************* END TURN REQUESTED`);
  const endTurnState = yield select();
  yield call(sendEndTurnMessage, endTurnState.players);
  yield put({ type: crosscutActions.CROSSCUT_END_TURN});
  return true;
}

function sendStartGameMessage(quizId: number) {
  const quiz = getQuizById(quizId);
  return pubsub.publish('nextMessage', {
    nextMessage: {
      gameId: quizId,
      description: quiz,
      gameToken: 'this will have something in it soon',
      otherPlayers: [
        {nickName: 'FOOBAR'},
        {nickName: 'BAZ'}
      ]
    }
  });
}

function sendNextQuestionMessage(question) {
  return pubsub.publish('nextMessage', {
    nextMessage: {
      question: {
        text: question.text,
        id: question.id,
        options: [...question.options]
      }
    }
  });
}

function sendTickTockMessage(timeLeft) {
  return pubsub.publish('nextMessage', {
    nextMessage: {
      timeLeft
    }
  });
}

function sendEndTurnMessage(players) {
  const winningPlayers = players
    .filter(p => p.lastAnswerCorrect)
    .map(p => { return { nickName: p.nickName, score: p.score}; });

  return pubsub.publish('nextMessage', {
    nextMessage: {
      winningPlayers,
      leaderBoard: [],
      yourAnswer: 'A',
      yourAnswerCorrect: false,
      yourScore: 10
    }
  });
}

function sendEndGameMessage(players) {
  return pubsub.publish('nextMessage', {
    nextMessage: {
      finalScores: players.map((p) => { return { nickName: p.nickName, score: p.score}; })
    }
  });
}

function sendGameState(state: GameState) {
  return pubsub.publish('gameState', state);
}

