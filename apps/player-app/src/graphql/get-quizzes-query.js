import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function getQuizzesQuery() {

 return client.query({
  fetchPolicy: 'no-cache',
  query: gql`
    {
      getQuizzes {
        name
      }
    }`
  });
}
