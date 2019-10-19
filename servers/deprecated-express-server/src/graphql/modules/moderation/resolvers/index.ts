import mutations from './mutations';
import {IResolvers} from 'apollo-server';

const resolvers: IResolvers = {
  Mutation: { ...mutations }
};

export default resolvers;
