import * as actions from './players-actions';
import * as crosscutActions from '../cross-cut-actions';
import omit from 'lodash/omit';
import { Player } from '../models/player';
import {Guid} from 'guid-typescript';
import logger from '../../server/logger';
import {DeepPartial} from "redux";

export default function playersReducer(state = [] as Player[], action): DeepPartial<Player[]> {
  switch (action.type) {
    case crosscutActions.CROSSCUT_START_REGISTRATION:
      return doResetPlayers(state, action);
    case actions.PLAYER_ACTION_REGISTER_PLAYER:
      return doRegisterPlayer(state, action);
    case actions.PLAYER_ACTION_RESET_PLAYERS_QUESTION_AND_ANSWER:
      return doResetPlayersQuestionAndAnswer(state, action);
    case actions.PLAYER_ACTION_ASSIGN_ANSWER_FOR_QUESTION:
      return doAssignAnswerForQuestion(state, action);
    case actions.PLAYER_ACTION_SCORE_CURRENT_PLAYER_QUESTION:
      return doScoreAnswerForQuestion(state, action);
    case crosscutActions.CROSSCUT_END_TURN:
      return doEndTurn(state, action);
    case actions.PLAYER_ACTION_END_GAME:
      return doEndGame(state, action);
  }
  return state;
}

function doResetPlayers(state, action) {
  // remove player list
  return [ ];
}

function doRegisterPlayer(state, action) {
  const nickNameIdx = state.indexOf((item) => item.nickName === action.nickName);
  if (nickNameIdx > -1) {
    throw new Error('Nickname found');
  } else {
    return [
      ...state, {
        nickName: action.nickName,
        token: action.token
      }
    ];
  }
}

function doResetPlayersQuestionAndAnswer(state, action) {
  return state.map((p) => omit(p, ['score', 'answer', 'success']));
}

/*
  Assign answer for player. If score > 0, we mark it as a success, so they know they won the challenge.
 */
function doAssignAnswerForQuestion(state, action) {
  if (state) {
    return state.map((p) => {
      return p.nickName === action.nickName ?
        {...p, answer: action.answer}
        :
        p;
    });
  } else {
    logger.error('player not found');
    return state;
  }
}

function doScoreAnswerForQuestion(state, action) {
  if (state && state.length > 0) {
   return state.map((p) =>  p.nickName === action.nickName ?
        { ...p,
          currentQuestionScore: action.score,
          score: action.score + (p.score || 0),
          success: action.score > 0 }
        :
        p)
  } else {
    logger.error(`no player data found in doScoreAnswerForQuestion`);
    return state;
  }
}

function doEndTurn(state, action) {
  return state.map(p => {
    const {answer, correct, currentQuestionScore, ...rest} = p;
    return rest;
  })
}

function doEndGame(state, action) {
  return state.map(p => ({ ...p, score: 0, success: false, answer: null }));
}
