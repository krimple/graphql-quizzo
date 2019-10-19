import { expectSaga } from 'redux-saga-test-plan';
import { put } from 'redux-saga/effects';
import * as actions from '../../quiz/quiz-actions';
import { gamePlaySaga } from '../gameplay-saga';

describe('Gameplay Saga', () => {
  beforeEach(() => {
    jest.setTimeout(10000);
  });

  it('should properly execute gameplay steps', (done) => {
    return expectSaga(gamePlaySaga)
        .dispatch({ type: actions.QUIZ_ACTION_REQUEST_GAME_START, quizId: 1})
        .withState({
          quizzes: [],
          players: {
              players: [
                { id: 'abcdefg', nickName: 'Joe', score: 0 }
              ]
          }
        })
        .put({ type: actions.QUIZ_ACTION_START_GAME, quizId: 1})
        .delay(5000)
        .run({timeout: 1000});
  });
});
