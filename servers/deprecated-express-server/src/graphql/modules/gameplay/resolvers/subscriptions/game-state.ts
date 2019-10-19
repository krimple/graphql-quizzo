// see https://www.apollographql.com/docs/apollo-server/features/subscriptions.html
const gameState = {
  subscribe: (parent, args, ctx) => {
    return ctx.PubSub.asyncIterator(['gameState']);
  }
};

export default gameState;
