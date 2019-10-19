import * as actions from '../players-actions';
import playersReducer from '../players-reducer';
import {REGISTER_PLAYER_MUTATION} from '../../../../../conference-quiz-client-angular/src/app/services/gql-statements';
import {PLAYER_ACTION_REGISTER_PLAYER} from '../players-actions';

describe('Players Reducer', () => {
  it('should register a player', () => {
    const startingState = {
      players: []
    };

    const newState = playersReducer(
        startingState,
        {
          type: actions.PLAYER_ACTION_REGISTER_PLAYER,
          nickName: 'baz',
          token: 'aaaaaaa'
        }
    );
    expect(newState.players.length).toBe(1);
    const player = newState.players[0];
    expect(player).toBeDefined();
    expect(newState.players[0].token).toBe('aaaaaaa');
    expect(newState.players[0].nickName).toBe('baz');
    expect(newState.players[0].id).toBeDefined();
  });
});
