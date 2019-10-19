import {Role} from './role';

export class User {
  constructor(
    public id: string,
    private password: string,
    private roles: string[]
  ) { }

  hasRole(role: string) {
    return this.roles.indexOf(role) > -1;
  }
  matchPassword(password: string) {
    return this.password === password;
  };
}
