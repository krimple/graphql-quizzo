import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function registerPlayerMutation(nickName, password) {
  return new Promise((resolve, reject) => {
    client.mutate({
      mutation: gql`
        mutation registerPlayer($nickName: String!, $password: String!) {
          registerPlayer(nickName: $nickName, password: $password) {
            nickName
            token
          }
        }
      `,
      variables: {
        nickName,
        password
      }
    })
    .then(
      result => {
        if (result.data && result.data.registerPlayer && result.data.registerPlayer.token) {
          const token = result.data.registerPlayer.token;
          localStorage.setItem('token', token);
          resolve();
        } else {
          console.log(`Unexpected result:  ${JSON.stringify(result)}`);
          localStorage.removeItem('token');
          reject('Invalid response');
        }
      },
      error => {
        console.log(`subscription failed. ${error}`);
        localStorage.removeItem('token');
        if (error.constructor.name === 'ApolloError') {
          reject(error.message);
        } else {
          console.error('unexpected error');
          console.dir(error);
          reject('Unexpected error.');
        }
        reject(error);
      });
  });
}
