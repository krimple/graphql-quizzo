import {Component, OnInit} from '@angular/core';
import {ModerationService} from '../services/moderation.service';
import {Subscription} from 'rxjs';
import {distinctUntilChanged} from 'rxjs/operators';
import {GameStatus} from "../../generated/graphql";

@Component({
  template: `
    <div class="container">
        <ng-container *ngIf="gameStatus?.gameMode === 'NOT_RUNNING' || gameStatus?.gameMode === 'GAME_OVER'">
            <button
              *ngFor="let quiz of this.gameList?.data?.getQuizzes"
              (click)="startRegistration(quiz.id)"
              class="btn btn-lg btn-block btn-primary"
            >
              Begin Registration for {{ quiz.title }}
            </button>
        </ng-container>
      
        <ng-container *ngIf="gameStatus?.gameMode === 'AWAITING_PLAYERS'">
           <button (click)="playGame()" class="btn btn-lg btn-block btn-primary">Start Game Play</button>
        </ng-container>

        <ng-container *ngIf="gameStatus?.gameMode === 'PRESENTING_QUESTION' || gameStatus?.gameMode === 'PRESENTING_SCORES'">
          <h2 class="display-2">{{ gameStatus.currentQuestion.text }}</h2>
        </ng-container>
      
      <hr/>
      <register-player></register-player>
      
    </div>
  `,
  styles: [`    
    .btn {
      margin-left: 1em;
      margin-right: 1em;
      background-color: #04AD69!important;
      padding: 30px 60px;
      font-weight: bold;
      text-transform: uppercase;
      border-radius: 0px;
      border: none;
    }
  `]
})
export class GameModeratorComponent implements OnInit {
  constructor(private moderationService: ModerationService) { }

  gameList: any;
  gameInProgress = false;
  gameStatus: GameStatus;
  subscription: Subscription;

  ngOnInit() {
    // give us two seconds - otherwise we get a superfluous auth error because we request before signing in...
    setTimeout(() => {
        this.moderationService.getQuizzes().subscribe(
          r => {
            this.gameList = r;
          },
          error => console.log('An error occurred during init of moderation component.', JSON.stringify(error))
        );
    }, 2000);
    this.moderationService.pollForGameStatus();
    this.subscribe();
  }

  startRegistration(quizId: number) {
    this.moderationService.selectGame(quizId).subscribe(
      result => {
        if (result) {
          this.gameInProgress = true;
        }
      },
      error => alert(JSON.stringify(error))
    );
  }

  playGame() {
    this.moderationService.beginGamePlay().subscribe(
      result => {
        if (result) {
          this.gameInProgress = true;
        }
      },
      error => alert(JSON.stringify(error))
    );
  }

  subscribe() {
    this.subscription = this.moderationService.gameStatusUpdates$
      .pipe(
        distinctUntilChanged()
      )
      .subscribe(
        result => {
          this.gameStatus = result;
        },
        (error) => {
          console.dir(error);
          alert(`error occurred: ${JSON.stringify(error)}`);
        },
        () => {
          console.log(`subscription ended.`);
          this.subscription = undefined;
        }
      );
  }
}

