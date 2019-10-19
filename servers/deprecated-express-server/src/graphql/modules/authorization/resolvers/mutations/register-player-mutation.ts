import {QuizManager} from '../../../../../quiz-manager';
import {generatePlayerToken, generateUserToken, handleLogin} from '../../../../../auth/security-helpers';
import logger from '../../../../../server/logger';
import * as playerActionCreators from '../../../../../store/players/players-action-creators';
import {GameMode} from '../../../../generated/graphql';

const quizManager = QuizManager.instance();

export default function registerPlayerMutation(_, payload, context) {
  return new Promise(async (resolve, reject) => {
    const requestedNickName = payload.nickName;
    const password = payload.password;
    let token;
    try {
      // generate a JWT if the credentials match
      token = await handleLogin(requestedNickName, password);
    } catch (e) {
      logger.error(`Invalid token ${JSON.stringify(e, undefined, 2)}`);
      // NOTE: if we don't exit here, we accidentally create a player...
      return reject(e);
    }

    // stop registration if game already underway, then
    // create the player if the nickName doesn't already exist
    try {
      const currentState = quizManager.getState();
      if (currentState.quiz.gameMode !== GameMode.AwaitingPlayers) {
        logger.info(`Game in progress, cannot add player`);
        reject(`Game in progress`);
        return false;
      }
      const currentPlayers = currentState.players;
      const foundPlayer = currentPlayers.findIndex((p) => p.nickName === requestedNickName);
      if (foundPlayer > -1) {
        reject(`Player ${payload.nickName} already exists`);
        return;
      } else {
        // create a token
        let token = await generatePlayerToken(payload.nickName);
        // create a player in redux
        const playerAction = playerActionCreators.registerPlayer(payload.nickName, token);
        quizManager.dispatch(playerAction);

        // now query the player in redux state to get the guid (playerId) for the JWT token
        const updatedState = quizManager.getState();
        const newPlayers = updatedState.players;
        const newPlayer = newPlayers.find((p) => p.nickName === requestedNickName) || undefined;
        if (!newPlayer) {
          reject('Unexpected condition:  New player not found');
          return;
        }
        const playerNickName = newPlayer.nickName;
        resolve({
          nickName: requestedNickName,
          token
        });
        return;
      }
    } catch (e) {
      logger.error('Registration failed...');
      logger.error(e);
      logger.error(JSON.stringify(e, undefined, 2));
      reject(`Unexpected error - check server log for details`);
    }
  });
}
