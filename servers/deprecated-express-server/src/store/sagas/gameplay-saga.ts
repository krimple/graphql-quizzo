import { take, takeEvery, delay, put, call, select } from 'redux-saga/effects';
import * as quizActions from '../quiz/quiz-actions';
import * as playerActions from '../players/players-actions';
import * as crosscutActions from '../cross-cut-actions';
import { pubsub } from '../../config/pubsub-holder';
import logger from '../../server/logger';
import {getQuizById} from '../store';
import store from '../store';

const QUESTION_DELAY_TIME = 1000;
const GAME_END_DELAY_TIME = 1000;
const QUESTION_DISPLAY_SECONDS = 500;

/** Avoids the while true endless loop - cancels any prior saga */
export function* gamePlaySaga() {
  return yield takeEvery(quizActions.QUIZ_ACTION_REQUEST_GAME_START, gamePlaySagaImpl);
}

function* gamePlaySagaImpl(payload) {
  const quizId = payload.quizId;
  yield call(gameSaga, quizId);
}

export function* gameSaga(quizId) {
  const quiz = getQuizById(quizId);
  const questions = quiz.questions;

  // TODO - refactor - naieve implementation
  const gameStartState = yield select();
  const nickNames = gameStartState.players.map((p) => p.nickName);
  yield call(sendStartGameMessage, quizId);
  yield put({
    type: quizActions.QUIZ_ACTION_START_GAME,
    quiz
  });
  yield delay(QUESTION_DELAY_TIME);
  for (const question of questions) {
    yield call(sendNextQuestionMessage, question);
    yield put({
      type: quizActions.QUIZ_ACTION_NEXT_QUESTION,
          question
    });
    yield call(turnSaga, nickNames);
    yield delay(QUESTION_DELAY_TIME);
  }
  const finalState = yield select();
  logger.info(`Final player data ${JSON.stringify(finalState.players)}`);
  yield call(sendEndGameMessage, finalState.players);
  yield put({ type: quizActions.QUIZ_ACTION_END_GAME });
  yield put({ type: playerActions.PLAYER_ACTION_END_GAME});
}

export function* turnSaga(nickNames) {
  let gameTimer = QUESTION_DISPLAY_SECONDS;
  while (gameTimer > 0) {
    // TODO - refactor - naive implementation
    // how many players left who haven't answered?
    const state: any = yield select();
    // TODO - pull back quiz to its own reducer
    const playersAwaitingAnswers = state.players.filter(p => !p.answer).length > 0;
    if (!playersAwaitingAnswers) {
      // get out if we don't have any more players with pending answers!
      break;
    }
    yield delay(GAME_END_DELAY_TIME);
    gameTimer--;
    yield call(sendTickTockMessage, gameTimer);
  }
  const endTurnState= yield select();
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
        {playerId: 2, nickName: 'FOOBAR'},
        {playerId: 3, nickName: 'BAZ'}
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
      finalScores: players.map((p) => { return { playerId: 999, nickName: p.nickName, score: p.score}; })
    }
  });
}

