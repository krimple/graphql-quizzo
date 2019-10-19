import * as actions from './players-actions';

// TODO - convert this to a saga like submitting a question
export function registerPlayer(nickName: string, token: string) {
  return {
    type: actions.PLAYER_ACTION_REGISTER_PLAYER,
    nickName,
    token
  };
}

export function submitQuestionForPlayer(nickName: string, answer: string) {
  return {
    type: actions.PLAYER_ACTION_ASSIGN_ANSWER_FOR_QUESTION,
    nickName,
    answer
  };
}

export function scoreCurrentPlayerQuestion(nickName: string, score: number) {
  return {
    type: actions.PLAYER_ACTION_SCORE_CURRENT_PLAYER_QUESTION,
    nickName,
    score
  };
}

export function endGame() {
  return {
    type: actions.PLAYER_ACTION_END_GAME
  };
}
