import * as actions from './actions';

const defaultState = {
    gameInProgress: false
};

export default function reducer(state = defaultState, action) {

   switch (action.type) {
       case actions.ACTION_SIGN_ON_BEGIN:
           return doSignOnBegin(state, action);
       case actions.ACTION_SIGN_ON_SUCCESS:
           return doSignOnSuccess(state, action);
       case actions.ACTION_SIGN_ON_FAILURE:
           return doSignOnFailure(state, action);
       case actions.ACTION_JOIN_GAME_BEGIN:
           return doJoinGameBegin(state, action);
       case actions.ACTION_JOIN_GAME_SUCCESS:
           return doJoinGameSuccess(state, action);
       case actions.ACTION_JOIN_GAME_FAILURE:
           return doJoinGameFailure(state, action);
       case actions.ACTION_POLL_FOR_GAME_STATUS_BEGUN:
           return doPollForGameStatusBegun(state, action);
       case actions.ACTION_GAME_STATUS_CHANGE:
           return doGameStatusChange(state, action);
       case actions.ACTION_POLL_FOR_GAME_STATUS_COMPLETE:
           return doPollForGameStatusComplete(state, action);
       case actions.ACTION_ASSERT_JWT_TOKEN_EXISTS:
           return doAssertJwtTokenExists(state, action);
       case actions.ACTION_ASSERT_JWT_TOKEN_NOT_FOUND:
           return doAssertJwtTokenNotFound(state, action);
       default:
           return state;
   }
}

function doSignOnBegin(state, action) {
    return {
        ...state,
        signOnInProgress: true
    }
}
function doSignOnSuccess(state, action) {
    const { signOnInProgress, signOnError, ...newState } = state;
    return {
        ...newState,
        token: action.token,
        nickName: action.nickName
    };
}

function doSignOnFailure(state, action) {
    const { token, nickName, signOnInProgress, ...newState} = state;
    return {
        ...newState,
        signOnError: action.error
    };
}

function doJoinGameBegin(state, action) {
    return {
        ...state,
        joinGameInProgress: true
    };
}

function doGameStatusChange(state, action) {
    const {type, ...actionData} = action;
    const {gameStatus} = actionData.gameStatus;
    return {
        ...state,
        ...gameStatus
    };
}

function doJoinGameSuccess(state, action) {
    const { joinGameInProgress, ...newState } = state;
    return {
        ...newState,
        gameInProgress: true
    };
}

function doJoinGameFailure(state, action) {
    const { joinGameInProgress, ...newState } = state;
    return {
        ...newState,
        gameInProgress: false
    }
}

function doPollForGameStatusBegun(state, action) {
    return {
        ...state,
        statusPollingActive: true
    };
}

function doPollForGameStatusComplete(state, action) {
    const { statusPollingActive, ...newState} = state;
    return newState;
}

function doAssertJwtTokenExists(state, action) {
    return {
        ...state,
        authenticated: true,
        nickName: action.nickName
    };
}

function doAssertJwtTokenNotFound(state, action) {
    return {
        ...state,
        authenticated: false
    }
}