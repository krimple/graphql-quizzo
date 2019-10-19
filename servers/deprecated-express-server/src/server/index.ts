import express from 'express';
import cors from 'cors';

import { pubsub } from '../config/pubsub-holder';
import { ApolloServer } from 'apollo-server-express';
import { parse, execute, subscribe } from 'graphql';
import { express as voyagerMiddleware } from 'graphql-voyager/middleware';
import logger from './logger';
import morgan from 'morgan';
import fs from 'fs';
import http from 'http';
import https from 'https';
import modules from '../graphql/modules';
import server from '../graphql';

// to keep typing happy
const apolloGraphQLServer = server as ApolloServer;

const jwt = require('express-jwt');

export default function() {
  // TODO - make it real, seriously Ken...

  const app = express();

  // install logger
  app.use(morgan('combined', {
    stream: { write: (message) => logger.info(message ? message.trim() : '') }})
  );

  // TODO - externalize config
  const PORT = 4000;

  // add JWT security - this never got a proper shakedown
  // app.post('/graphql', verifyToken, handleIndex);
  // app.use('/graphql', jwt({
  //   // TODO externalize this here and in jwt-helpers to an external env / setting
  //   secret: 'I am really secret',
  //   credentialsRequired: false
  // }));

  app.use('/voyager', voyagerMiddleware({ endpointUrl: '/graphql' }));

  // TODO Add CORS middleware maybe not anymore?
  apolloGraphQLServer.applyMiddleware({
    app,
    cors: {
      origin: '*',
      methods: ['GET,HEAD,PUT,PATH,POST,DELETE_OPTIONS'],
      optionsSuccessStatus: 200
    }
  });

  const httpServer = http.createServer(app);
  apolloGraphQLServer.installSubscriptionHandlers(httpServer);
  httpServer.listen(4000, () => {
    logger.info(`Server ready at http://localhost:4000${apolloGraphQLServer.graphqlPath}`);
    logger.info(`Subscriptions ready at ws://localhost:4000${apolloGraphQLServer.subscriptionsPath}`);
  });

  // place your keys in a keys directory (perhaps outside of the code base like we did with the Spring sample)
  // so you can load them...
  const httpsServer = https.createServer({
    key: fs.readFileSync('./keys/devserverkey.pem'),
    cert: fs.readFileSync('./keys/cert.pem'),
    passphrase: 'thisispaassphrase'    // TODO set externally
  }, app);

  apolloGraphQLServer.installSubscriptionHandlers(httpsServer);

  httpsServer.listen(4443, () => {
    logger.info(`SSL Server ready on https://localhost:4443${apolloGraphQLServer.graphqlPath}`);
    logger.info(`Subscriptions ready on wss://localhost:4443${apolloGraphQLServer.subscriptionsPath}`);
  });

  // config.pubsub = pubsub;
}
