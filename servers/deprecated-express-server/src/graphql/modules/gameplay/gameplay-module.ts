import { importSchema } from 'graphql-import';
import {GraphQLModule} from '@graphql-modules/core';
import AuthorizationModule from '../authorization';
import resolvers from './resolvers';

import typeResolvers from './resolvers/type-resolvers';
import { pubsub } from '../../../config/pubsub-holder';
const parsedSchema = importSchema('./src/graphql/modules/gameplay/gameplay.graphql');

export default new GraphQLModule({
  typeDefs: parsedSchema,
  imports: [
    AuthorizationModule
  ],
  resolvers: { ...resolvers, ...typeResolvers },
  context: ((session, currentContext, moduleSessionInfo) => {
    return {
      PubSub: pubsub
    };
  })
});
