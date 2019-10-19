import modules from './modules';
import { ApolloServer } from 'apollo-server-express';
const { schema, context, subscriptions, typeDefs, resolvers } = modules;

// initialize GraphQL Server
export default new ApolloServer({
  schema, context, subscriptions, typeDefs, resolvers });
