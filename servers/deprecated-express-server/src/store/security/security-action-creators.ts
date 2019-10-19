import { getUsersData } from '../../db/game-repository';
import * as actions from './security-actions';
import logger from '../../server/logger';

export function loadUsers () {
  return async dispatch => {
    try {
      const users = await getUsersData();
      dispatch({
        type: actions.SECURITY_ACTION_LOAD_USERS,
        users: users
      });
    } catch (e) {
      logger.error(e);
      return;
    }
  }
}
