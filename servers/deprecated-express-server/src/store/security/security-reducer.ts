import {User} from '../../graphql/generated/graphql';
import * as actions from './security-actions';

export default function securityReducer(state = [] as User[], action) {
  switch (action.type) {
    case actions.SECURITY_ACTION_LOAD_USERS:
      return doLoadUsers(state, action);
    default:
      return state;
  }
}

function doLoadUsers(state, action) {
  return [...action.users];
}


