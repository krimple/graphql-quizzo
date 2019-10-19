import * as actions from '../actions';
import {pollForGameStatus} from '../../graphql';
import {distinctUntilChanged,tap} from 'rxjs/operators';

let subscription;
export function beginPollingStatus() {
  return function(dispatch) {
      return new Promise((resolve, reject) => {
          subscription = pollForGameStatus()
              .pipe(
                  distinctUntilChanged(),
                  tap(statusMessage => {
                      const gameStatus = statusMessage.data;
                      const gameMode = gameStatus.gameMode;

                      dispatch({
                          type: actions.ACTION_GAME_STATUS_CHANGE,
                          gameMode,
                          gameStatus,
                          gameTitle: gameStatus.gameTitle || undefined,
                          gameDescription: gameStatus.gameDescription || undefined,
                          currentQuestion: gameStatus.currentQuestion || undefined,
                          currentOptions: gameStatus.currentOptions || undefined,
                          questionScore: gameStatus.questionScore || undefined,
                          finalScore: gameStatus.finalScore || undefined
                      });
                  })
              )
              // TODO - Livin' in a Hell Hole!  Refactor this per David St. Hubbins
              .subscribe(
                  statusMessage => {
                      console.log(`New message arrived: ${JSON.stringify(statusMessage, null, 2)}`);
                 },
                  (error) => reject(error)
              );

          dispatch({
              type: actions.ACTION_POLL_FOR_GAME_STATUS_BEGUN,
              subscription
          });

          resolve();
      });
  }
}

export function endPolling() {
    return function (dispatch, getState) {
        if (subscription) {
            subscription.disconnect();
        }

        dispatch({
            type: actions.ACTION_POLL_FOR_GAME_STATUS_COMPLETE
        });

    };
}
