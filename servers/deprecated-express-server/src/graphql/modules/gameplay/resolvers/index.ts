import queries from './queries';
import mutations from './mutations';
import subscriptions from './subscriptions';
import typeResolvers from './type-resolvers';
import {IResolvers} from 'apollo-server';
import logger from '../../../../server/logger';

const resolvers: IResolvers = {
  Query: { ...queries },
  Mutation: { ...mutations },
  Subscription: { ...subscriptions }
};

export default resolvers;
