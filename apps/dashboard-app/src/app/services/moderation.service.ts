import {Injectable} from '@angular/core';

import gql from 'graphql-tag';
import {Apollo} from 'apollo-angular';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {map, tap} from 'rxjs/operators';
import {throwError} from 'rxjs';
import {Subject} from 'rxjs';
import {MessageService} from 'primeng/api';
import {GameStatus} from '../../generated/graphql';

const MUTATION_SELECT_GAME = gql`
  mutation selectGame($quizId: Int!) {
    selectGame(quizId: $quizId)
  }
`;

const MUTATION_BEGIN_GAMEPLAY = gql`
  mutation beginGamePlay {
    beginGamePlay
  }
`;

const MUTATION_RESET = gql`
  mutation reset {
    reset
  }
`;

const QUERY_GET_QUIZZES = gql`
  query getQuizzes {
    getQuizzes {
      id
      title
      description
    }
  }
`;

const MUTATION_ANSWER_QUESTION = gql`
  mutation answerQuestion($answer: String!) {
    answerQuestion(answer: $answer)
  }
`;

const MUTATION_NEXT_QUESTION = gql`
  mutation {
    nextQuestion
  }
`;

const MUTATION_END_TURN = gql`
  mutation {
    endTurn
  }
`;

const MUTATION_MODERATOR_ADD_PLAYER = gql`
  mutation registerPlayer($details: NewUserData!) {
    registerPlayer(details: $details)
  }
`;

const GET_GAME_STATUS = gql`
query  {
  panelGameStatus {
    gameMode
    gameDescription
    currentQuestion {
      text
    }
   currentOptions {
        label
        key
      }
    questionScores {
      nickName
      score
      answer
      answerText
    }
    finalScores {
      nickName
      score
    }

  }
}`;


@Injectable({providedIn: 'root'})
export class ModerationService {
  constructor(
    private apollo: Apollo,
    private router: Router,
    private httpClient: HttpClient,
    private messageService: MessageService) {

    // begin watching game system on next tick
    setTimeout(() => {
      this.pollSubscription = this.pollForGameStatus();
    }, 0);

  }

  public readonly gameStatusUpdates$ = new Subject<GameStatus>();
  private pollSubscription: any;

  private checkForError(result: any) {
    if (result.constructor.name === 'ApolloError') {
      this.messageService.add({ severity: 'error', summary: 'Apollo protocol error', detail: JSON.stringify(result)});
      throwError(result);
    } else if (!result.data) {
      this.messageService.add({ severity: 'error', summary: 'Not a data frame', detail: JSON.stringify(result) });
      console.log(`Not a data frame... details: ${result}`);
      throwError(result);
    }
  }

  pollForGameStatus() {
    this.pollSubscription = this.apollo.watchQuery({
      query: GET_GAME_STATUS,
      pollInterval: 1000
    }).valueChanges.subscribe(
      value => {
         const gameStatusValue = value.data['panelGameStatus'];
         this.gameStatusUpdates$.next(gameStatusValue);
       },
      error => {
        console.log('An error occurred attempting to subscribe to the game status', JSON.stringify(error));
        if (this.pollSubscription) {
          try {
            this.pollSubscription.complete();
          } catch (e) {
            console.log('Threw error on failure, but who cares, this is last chance cleanup. ', JSON.stringify(e));
          }
          this.pollSubscription = undefined;
        }
      });
  }

  endPolling() {
    if (this.pollSubscription) {
      this.pollSubscription.complete();
      this.pollSubscription = undefined;
    }
  }
}
