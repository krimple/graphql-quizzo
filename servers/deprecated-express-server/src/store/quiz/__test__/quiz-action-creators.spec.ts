import * as actions from '../quiz-actions';
import * as actionCreators from '../quiz-action-creators';

describe('Quiz Action Creators', () => {
  it('should accept an answer', () => {
    const action = actionCreators.acceptAnswer('baccceee', 'Q');
    expect(action.type).toBe(actions.QUIZ_ACTION_ACCEPT_ANSWER);
    expect(action.playerId).toBe('baccceee');
    expect(action.answer).toBe('Q');
  });
});
