import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function gameplayMessageSubscription() {
  return client.subscribe({
    fetchPolicy: 'no-cache',
    query: gql`
      subscription onNextMessage {
        nextMessage {
          ... on TurnOver {
            winningPlayers {
                nickName
                score
            }
            leaderBoard {
              nickName
              lastAnswer
              lastAnswerCorrect
              lastAnswerPoints
              score
            }
          }
          ... on SubmitQuestion {
            question {
              ... on MultipleChoiceQuestion {
                text
                options {
                  key
                  label
                }
              }
            }
          }
          ... on TickTock {
            timeLeft
          }
          ... on GameStart {
            gameId
            gameToken
            otherPlayers {
              nickName
            }
          }
          ... on GameOver {
            finalScores {
              nickName
              score
            }
          }
        }
      }
    `
  });
}

