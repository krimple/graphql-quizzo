import {User} from './generated/graphql';
import {Player} from './generated/graphql';

export interface GameContext {
  user?: User,
  player?: Player
}
