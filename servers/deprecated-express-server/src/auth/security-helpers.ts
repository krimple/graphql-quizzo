import jwt from 'jsonwebtoken';
import logger from '../server/logger';
import {QuizManager} from '../quiz-manager';
import store from '../store/store';
import {User} from "../graphql/generated/graphql";

import {authenticate } from './authentication-service';
import {Player} from '../../../codegen/src/generated/graphql';

const quizManager = QuizManager.instance();

// TODO REALLY EXTERNALIZE THIS ONE AND BY ENVIRONMENT
// Make it a game joining key that changes per game randomly - short string that shows on console

// JWT signing key
const SECRET_KEY = 'I am really secret';

// demo test user password credentials - any user + this password gets in for now
const STRAW_PASSWORD = 'testing123';

// TODO - remove this and back with Redux? keyed by playerId GUID
const playerDB = {};
const userDB = {};

// TODO - externalize data into redux store
// generate a token for the user - wipe out the prior token automatically
// TODO- players are currently BORKED while ADMIN is being worked out
export async function generatePlayerToken(nickName: string): Promise<any> {
  const token = await jwt.sign({ nickName }, SECRET_KEY, { expiresIn: '3d'});
  playerDB[nickName] = token;
  return token;
}

export async function generateUserToken(user: User): Promise<any> {
  const token = await jwt.sign({user: user.id}, SECRET_KEY, {expiresIn: '5d'});
  // in our system, only the user who generated the token can authenticate jwt with it
  // TODO - harden this further
  userDB[user.id] = token;
  return token;
}

// @ts-ignore
export function verifyNakedToken(token): Promise<any> {
  // @ts-ignore
  return new Promise((resolve, reject) => {
    const tokenData = jwt.verify(token, SECRET_KEY, (err, decoded) => {
      if (err) {
        reject('no token or token invalid');
      } else {
        logger.info(`Token verified. ${tokenData.nickName} - ${tokenData.quizId}`);
        resolve(decoded);
      }
    });
  });
}

export function verifyToken(token) {
  if (!token) {
    throw new Error('No auth token supplied');
  }

  if (token.startsWith('Bearer ')) {
    // remove prefix
    token = token.slice(7, token.length);
  }

  jwt.verify(token, SECRET_KEY, (err, decoded) => {
    if (err) {
      throw new Error('Invalid token');
    }
    return;
  });
}

export async function handleLogin(id, password) {
  const authInfo =  authenticate(id, password);
  if (authInfo) {
    return await generateUserToken(authInfo);
  }
}

export async function handlePlayerRegistration(nickName, password) {
   if (!password) {
    throw new Error('No password sent.');
  }
  if (!nickName) {
    throw new Error('no nickname sent.');
  }
  if (password !== STRAW_PASSWORD) {
    throw new Error( 'Authentication failed - password unmatched');
  }
  return await generatePlayerToken(nickName);
}

export function handleIndex(req, res) {
  res.json({
    success: true,
    message: 'Index page'
  });
}

// pattern - get JWT token, use it to decode and get the playerId and

export async function tradeTokenForPlayer(token: string): Promise<Player> {
  return new Promise((resolve, reject) => {
    if (!token) {
      reject('No auth token supplied');
    }

    if (token.startsWith('Bearer ')) {
      // remove prefix
      token = token.slice(7, token.length);
    }

    jwt.verify(token, SECRET_KEY, (err, decoded) => {
      if (err) {
        logger.error(err);
        reject('Invalid token');
      }

      const player: Player = store
        .getState()
        .players
        .find(p => p.nickName === decoded.nickName);

      resolve(player);
    });
  });
}

export async function tradeTokenForUser(token: string): Promise<User> {
  return new Promise((resolve, reject) => {
    if (!token) {
      reject('No auth token supplied');
    }

    if (token.startsWith('Bearer ')) {
      // remove prefix
      token = token.slice(7, token.length);
    }

    jwt.verify(token, SECRET_KEY, (err, decoded) => {
      if (err) {
        logger.error(err);
        reject('Invalid token');
      }

      const user: User = store
        .getState()
        .security
        .find(p => p.id === decoded.id);

      // TODO- players are currently BORKED while ADMIN is being worked out
      resolve(user);
    });
  });
}
