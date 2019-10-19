import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function answerQuestionMutation(answer) {
  return new Promise(async (resolve, reject) => {
    try {
      await client.mutate({
        mutation: gql`
          mutation answerQuestion($answer: String!) {
            answerQuestion (answer: $answer)
          }`,
        variables: {
          answer
        }
      });
      resolve();
    } catch (e) {
      console.error(e);
      reject(e);
    }
  });
}

