import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';
import {Subject} from 'rxjs';

const gameStatusUpdates$ = new Subject();

// TODO - I smell like month-old cheetos, refactor me
// TODO - also, make a cancel operation
let zenSubscription = null;

const GET_GAME_STATUS = gql`
  query {
    gameStatus {
      gameMode
      gameTitle
      gameDescription
      currentQuestion {
        text
      }
      currentOptions {
        label
        key
      }
      questionScore {
        nickName
        score
        answer
        answerText
      }
      currentQuestionAnswered
      finalScore
    }
  }
`;

export function pollForGameStatus() {
    zenSubscription = client.watchQuery({
        query: GET_GAME_STATUS,
        pollInterval: 1000
    });

    zenSubscription = zenSubscription.subscribe(
      value => {
          gameStatusUpdates$.next(value);
          // once we're done, log out and do no harm to phone batteries by stopping the refresh
          if (value.data.gameStatus.gameMode === 'GAME_OVER') {
              zenSubscription.unsubscribe();
              localStorage.removeItem('token');
          }
         },
      error => {
          console.log('An error occurred attempting to subscribe to the game status', JSON.stringify(error));
          if (zenSubscription) {
              try {
                  zenSubscription.unsubscribe();
              } catch (e) {
                  console.log('Threw error on failure, but who cares, this is last chance cleanup. ', JSON.stringify(e));
              }
          }
      });
    return gameStatusUpdates$;
}

