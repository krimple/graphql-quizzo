import mutations from './mutations';
import queries from './queries';
import {IResolvers} from 'apollo-server';
import logger from '../../../../server/logger';

const resolvers: IResolvers = {
  Mutation: { ...mutations },
  Query: { ...queries }
};

export default resolvers;
