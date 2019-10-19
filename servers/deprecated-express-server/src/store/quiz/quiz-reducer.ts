import * as actions from './quiz-actions';
import * as crossCutActions from '../cross-cut-actions';
import {GameMode} from '../../graphql/generated/graphql';

const defaultState = {
  quiz: null,
  gameMode: GameMode.Idle,
  questionIndex: null,
  timeLeft: 0,
  gameList: []
};

export default function quizReducer(state = defaultState, action) {
  if (!action) {
    console.log('No action passed');
  }

  if (action && !action.type) {
    console.log(`passed action has no type. ${JSON.stringify(action, undefined, 2)}`);
    return state;
  }

  switch (action.type) {
    case actions.QUIZ_ACTION_LOAD_GAME_LIST:
      return doLoadGameList(state, action);
    case crossCutActions.CROSSCUT_START_REGISTRATION:
      return doStartRegistration(state, action);
    case actions.QUIZ_ACTION_START_GAME:
      return doStartGame(state, action);
    case actions.QUIZ_ACTION_TICK_TOCK:
      return doTickTock(state, action);
    case actions.QUIZ_ACTION_NEXT_QUESTION:
      return doNextQuestion(state, action);
    case actions.QUIZ_ACTION_ACCEPT_ANSWER:
      return doAcceptAnswer(state, action);
    case crossCutActions.CROSSCUT_END_TURN:
      return doEndTurn(state, action);
    case actions.QUIZ_ACTION_END_GAME:
      return doEndGame(state, action);
    default:
      return state;
  }
}

function doLoadGameList(state, action) {
  return {
    ...state,
    gameList: action.gameList
  };
}
function doStartRegistration(state, action) {
  const quiz = action.quizzes.find(q => q.id === action.quizId);
  if (!quiz) {
    console.log('Quiz not found...');
    return state;
  }

  return {
    ...state,
    gameMode: GameMode.AwaitingPlayers,
    quiz
  };
}

function doStartGame(state, action) {
  return {
    ...state,
    gameMode: GameMode.AwaitingQuestions,
    questionIndex: -1
  };
}

function doTickTock(state, action) {
  return {
    ...state,
    timeLeft: action.timeLeft
  };
}

//TODO REFACTOR TO GRAB NEXT QUESTION IN SEQUENCE UNTIL END
function doNextQuestion(state, action) {
  const nextQuestionIndex = state.questionIndex;
  console.dir(state);
  if (nextQuestionIndex > state.questions.length - 1) {
    return doEndTurn(state, action);
  }

  return {
    ...state,
    quiz: {
      ...state.quiz,
      answers: {}
    },
    question: { ...state.quiz.question[nextQuestionIndex] },
    gameMode: GameMode.AwaitingAnswers,
    turnInProgress: true
  };
}

function doAcceptAnswer(state, action) {
  return {
    ...state,
    answers: {...state.answers, [action.nickName]: action.answer}
  };
}

function doEndTurn(state, action) {
  // game over immediately if we've scored the last question...
  if (state.questionIndex > state.quiz.questions.length - 1) {
    return doEndGame(state, action);
  } else {
    return {
      ...state,
      gameMode: GameMode.DisplayingAnswers,
      turnInProgress: false
    };
  }
}

function doEndGame(state, action) {
  return {
    ...state,
    gameMode: GameMode.DisplayingFinalScore,
    gameState: 'ended'
  };
}
