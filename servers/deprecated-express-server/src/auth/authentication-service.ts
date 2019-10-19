import {User} from '../graphql/generated/graphql';
import {findUserById} from '../store/store';

export function authenticate(id, pass): User | null {
  const user = findUserById(id);
  if (user && user.password === pass) {
    return { id: user.id, roles: user.roles} as User;
  } else {
    return null;
  }
}

