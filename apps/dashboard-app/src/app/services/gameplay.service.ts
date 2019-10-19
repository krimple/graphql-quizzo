import {Injectable} from '@angular/core';
import {Observable, ReplaySubject, Subject} from 'rxjs';
import * as gqlStatements from './gql-statements';
import {Apollo} from 'apollo-angular';
import {Question} from '../../generated/graphql';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {take, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class GameplayService {
  // gameEvents$: ReplaySubject<GamePlayMessage> = new ReplaySubject(1);
  public token: String;

  public currentQuestion: Question | null = null;
  public readonly _answerChanges$ = new Subject<string>();
  private _playerId: string | null = null;
  private _currentAnswer: string | null = null;
  private _nickName: string | null = null;

  public get nickName() {
    return this._nickName;
  }

  public get answerChanges(): Observable<string> {
    return this._answerChanges$;
  }

  public set currentAnswer(answer: string) {
    this._answerChanges$.next(answer);
    this._currentAnswer = answer;
  }

  private apolloSubscription: any;

  constructor(private apollo: Apollo, private router: Router, private httpClient: HttpClient) { }

  begin() {
    // design decision - one subscription = 1 websocket. Game chatter goes over the single
    // web socket to avoid having handlers for each message type. Not particularly beautiful
    // but heck, it's my silly demo...
    /*this.apolloSubscription = this.apollo.subscribe({    // apollo subscription
      fetchPolicy: 'no-cache',
      query: gqlStatements.GAME_EVENTS_SUBSCRIPTION
    })
      .subscribe((result) => {                                 // rxjs stream subscription
        const nextMessage: GamePlayMessage = result.data.nextMessage;
        // here's where my nice demo falls down a tad...
        // polymorphic types based on interfaces don't exhibit themselves at runtime,
        // so we have to infer based on data sent... Yuck?
        // also, the base types don't export the __typename so you have to get it by digging
        // using ECMAScript variable trickery
        switch (nextMessage['__typename']) {
          case 'SubmitQuestion':
            this.router.navigateByUrl('/game/question');
            console.log('Question submitted');
            this.assignQuestion(nextMessage['question'] as Question);
            break;
          case 'TurnOver':
            this.router.navigateByUrl('/game/turnover');
            console.log('Was a turn over message');
            break;
          case 'GameStart':
            console.log('Was a game start message');
            break;
          case 'GameOver':
            console.log('Ending game');
            this.router.navigateByUrl('/game/end');
            break;
        }
        this.gameEvents$.next(nextMessage);
      });
      */
  }

  end() {
    // TODO - what to do with apollo subscription?
    // this.gameEvents$.complete();
  }


  // TODO - tie in to client call - probably return subscription like with
  // other services or convert to promise...
  registerPlayer(nickName: string, password: string) {
    return new Promise((resolve, reject) => {
      this.apollo.mutate({
        mutation: gqlStatements.AUTHENTICATE_USER,
        variables: {
          credentials: {
            nickName,
            password
          }
        }
      })
      .subscribe(
        result => {
          console.log(`got jwt token ${result}`);
          if (result.constructor.name === 'ApolloError') {
            reject(result.message);
          } else if (result.data && result.data.registerPlayer && result.data.registerPlayer.token) {
            this.token = result.data.registerPlayer.token;
            console.log(`Look, ma! ${this.token}`);
            resolve(nickName);
          } else {
            console.log(`Unexpected result:  ${JSON.stringify(result)}`);
            reject(result);
          }
         },
         error => {
          console.log(`subscription failed. ${error}`);
          reject(error);
         });
    });
  }

  //   return this.apollo.mutate({
  //     mutation: REGISTER_PLAYER_MUTATION,
  //     variables: {
  //       nickName
  //     }
  //   }).toPromise()
  //     .then((response) => {
  //       console.log('Player registration returned');
  //       this._playerId = response.data.registerPlayer.playerId;
  //       return this._playerId;
  //     });

  requestGameStart() {
    this.apollo.mutate({
      mutation: gqlStatements.REQUEST_START_GAME_MUTATION,
      variables: {
        quizId: 1
      }
    })
      .subscribe(
        (result) => {
          this.router.navigateByUrl('/game/awaitquestion');
        });
  }

  answerQuestion(answer: string) {
    this.apollo.mutate({
      mutation: gqlStatements.ANSWER_QUESTION_MUTATION,
      variables: {
        answer: {
          answer,
          playerId: this._playerId
        }
      }
    })
      .subscribe(
        (result) => {
          if (!result) {
            throw new Error('mutation for answering question failed.');
          }
          this.currentAnswer = answer;
          this.router.navigateByUrl('game/answered');
        },
        (error) => {
          console.error(error);
          throw error;
        }
      );
  }

  private assignQuestion(question: Question) {
    this.currentQuestion = question;
  }
}
