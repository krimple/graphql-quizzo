import {GraphQLModule} from '@graphql-modules/core';
import gameplayModule from './gameplay';
import moderationModule from './moderation';

// authorization module was never integrated properly...

const module: GraphQLModule = new GraphQLModule({
  imports: [gameplayModule, moderationModule]
});

export default module;
