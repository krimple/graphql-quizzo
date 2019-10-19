import quizReducer from '../quiz-reducer';
import * as actions from '../quiz-actions';
import {GameMode} from '../../../graphql/generated/graphql';

describe('quiz reducer', () => {
  it('should accept answer', () => {
    const initialState = {
      quiz: {
        questions: [
          {  }
        ],
        answers: {}
      },
      gameMode: GameMode.AwaitingAnswers,
      timeLeft: 20,
      question: {},
      turnInProgress: true,
      answers: {}
    };
    const newState = quizReducer(
        initialState,
        {
          type: actions.QUIZ_ACTION_ACCEPT_ANSWER,
          nickName: '122112',
          answer: 'Q'
        });
    expect(newState).toBeDefined();
    expect(newState.answers).toEqual({
      '122112': 'Q'
    });
    console.log(newState);
  });

});
