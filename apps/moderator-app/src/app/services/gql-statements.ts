import gql from 'graphql-tag';

export const AUTHENTICATE_USER = gql`
mutation login($credentials: SignInCredentials){
  login(credentials: $credentials)
}`;

export const GAME_EVENTS_SUBSCRIPTION = gql`
  subscription onNextMessage {
    nextMessage {
      ... on TurnOver {
        winningPlayers {
          nickName
          score
        }
        leaderBoard {
          nickName
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
`;

export const GET_QUIZZES_QUERY = gql`
  query {
   getQuizzes {
    id
    title
    description
      questions {
        text
        options {
          label
        }
      }
    }
  }
`;

export const ANSWER_QUESTION_MUTATION = gql`
  mutation answerQuestion($answer: QuestionAnswerInput!) {
    answerQuestion (input: $answer)
  }
`;

export const REGISTER_PLAYER_MUTATION = gql`
  mutation registerPlayer($nickName: String!, $password: String!) {
    registerPlayer(nickName: $nickName, password: $password) {
      nickName
      token
    }
  }
`;

export const REQUEST_START_GAME_MUTATION = gql`
  mutation startGame($quizId: Int!) {
    startGame(quizId: $quizId)
  }
`;
