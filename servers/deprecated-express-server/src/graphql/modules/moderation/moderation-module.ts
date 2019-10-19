import { importSchema } from 'graphql-import';
import {GraphQLModule} from '@graphql-modules/core';
import AuthorizationModule from '../authorization';
import resolvers from './resolvers';

import { pubsub } from '../../../config/pubsub-holder';
const parsedSchema = importSchema('./src/graphql/modules/moderation/moderation.graphql');

export default new GraphQLModule({
  typeDefs: parsedSchema,
  imports: [
    AuthorizationModule
  ],
  resolvers: { ...resolvers },
  context: ((session, currentContext, moduleSessionInfo) => {
    return {
      PubSub: pubsub
    };
  })
});
