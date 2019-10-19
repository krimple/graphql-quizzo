import { GraphQLModule } from '@graphql-modules/core';
import {tradeTokenForPlayer, tradeTokenForUser} from '../../../auth/security-helpers';
import { importSchema } from 'graphql-import';
import resolvers from './resolvers';
import logger from '../../../server/logger';
import {GameContext} from '../../game-context';
import {Player, User} from '../../generated/graphql';

const HEADER_NAME = 'authorization';

const schema = importSchema('./src/graphql/modules/authorization/auth.graphql');

export default new GraphQLModule<{}, {}, GameContext>({
  name: 'auth',
  typeDefs: schema,
  resolvers: {
    ...resolvers,
    // TODO - type resolver?  How does this work and what is it called?
    Player: {
      nickName: (player) => player.nickName,
    },
  },
  context: async function (connection) {
    return await configureContext(connection);
  }
});

async function configureContext(connection): Promise<GameContext> {
  return new Promise(async (resolve, reject) => {

    let request;
    // TODO - figure out websocket subscription auth  as part 2 of this challenge
    if (connection.request && connection.socket) {
      request = connection.request;
      logger.info('Client is a websocket, skipping auth for now');
      resolve({} as GameContext);
    } else {
      request = connection.req;
      const authToken = request.headers.authorization;

      // only check token if passed... this allows public API calls

      // Two major types of user - a Player and a system User (game moderator/admin)
      // we have to try both types since we just know it's a JWT token but not what
      // is encoded in it until we try to trade it for a type...

      // Maybe this would be better done with two separate servers anyway in a real app
      // where each had different features
      if (!authToken) {
        logger.info(`No auth token for this request`);
        resolve({} as GameContext);   // anonymous user
      } else {
        let authData: GameContext;
        try {
          const player = await tradeTokenForPlayer(authToken);
          logger.info(`Authenticated player: ${JSON.stringify(player)}`);
          resolve({player} as GameContext);
        } catch (e) {
          logger.info(`Token ${authToken} is not a player... trying User...`);
        }
        try {
          const user = await tradeTokenForUser(authToken);
          logger.info(`Authenticated user: ${JSON.stringify(user)}`);
          resolve({user} as GameContext);
        } catch (e) {
          logger.error(e);
          logger.error(`Unable to authenticate using auth token: ${authToken}`);
          reject('Token is neither a player or user');
        }
      }
    }
  });
}
