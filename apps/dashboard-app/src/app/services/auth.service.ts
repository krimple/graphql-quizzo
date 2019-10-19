import {Injectable} from '@angular/core';
import * as gqlStatements from './gql-statements';
import {Apollo} from 'apollo-angular';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Injectable({ providedIn: 'root'})
export class AuthService {
  constructor(private apollo: Apollo, private router: Router, private httpClient: HttpClient) {

    if (localStorage.getItem('token')) {
      this.token = localStorage.getItem('token');
    }

  }

  token: string;

  clearToken() {
    this.token = undefined;
    localStorage.removeItem('token');
  }

  login(userName: string, password: string) {
    return new Promise((resolve, reject) => {
      this.apollo.mutate({
        mutation: gqlStatements.AUTHENTICATE_USER,
        variables: {
          credentials: {
            userName,
            password
          }
        }
      })
        .subscribe(
          result => {
            if (result.constructor.name === 'ApolloError') {
              reject(result.message);
            } else if (result.data && result.data.login) {
              this.token = result.data.login;
              localStorage.setItem("token", this.token);
              resolve(true);
            } else {
              reject(result);
            }
          },
          error => {
            console.log(`subscription failed. ${error}`);
            reject(error);
          });
    });
  }
}
