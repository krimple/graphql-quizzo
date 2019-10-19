import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function startGameMutation() {
  // TODO - select a specific game by UI
  const quizId = 1;
  return new Promise((resolve, reject) => {
    client.mutate({
      mutation: gql`
        mutation startGame($quizId: Int!) {
          startGame(quizId: $quizId)
        }
      `,
      variables: {
        quizId
      }
    })
    .then(
      result => {
        resolve();
      },
      error => {
        console.log(`mutation failed. ${error}`);
        // ? localStorage.removeItem('token');
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
