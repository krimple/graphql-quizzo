// see https://www.apollographql.com/docs/apollo-server/features/subscriptions.html
const nextMessage = {
  subscribe: (parent, args, ctx) => {
    return ctx.PubSub.asyncIterator(['nextMessage']);
  }
};

export default nextMessage;
