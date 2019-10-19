import {Component, OnInit} from '@angular/core';
import {ModerationService} from '../services/moderation.service';
import {Subscription} from 'rxjs';
import {distinctUntilChanged} from 'rxjs/operators';
import {GameStatus} from '../../generated/graphql';

@Component({
  template: `
    <div class="container-fluid">
      <div *ngIf="!gameStatus">Please wait...</div>
      <div *ngIf="gameStatus" class="jumbotron">

        <!-- QUESTION -->
        <ng-container *ngIf="gameStatus?.gameMode === 'PRESENTING_QUESTION'">
          <h4 class="display-4">Question...</h4>
          <h2 class="display-2">{{ gameStatus.currentQuestion.text }}</h2>
          <ul>
            <li *ngFor="let option of gameStatus.currentOptions">
              <h3 class="display-3">{{ option.key }} - {{ option.label }}</h3>
            </li>
          </ul>
        </ng-container>

        <ng-container *ngIf="gameStatus?.gameMode === 'AWAITING_PLAYERS'">
          <h2>Waiting for players to join</h2>
          <p>Visit https://quizzo.chariotsolutions.com/player or scan this QR code:</p>
          <img src="./assets/player-qr-code.png" />
        </ng-container>

        <!-- SCORES -->
        <ng-container *ngIf="gameStatus?.gameMode === 'PRESENTING_SCORES'">

          <h4 class="display-4">Question...</h4>
          <h2 class="display-2">{{ gameStatus.currentQuestion.text }}</h2>

          <table class="answerTable">
            <tr>
              <th>Nickname</th>
              <th>Answer</th>
              <th>Correct?</th>
            </tr>
            <tr *ngFor="let score of gameStatus.questionScores">
              <td>{{score.nickName}}</td>
              <td>{{score.answerText}}</td>
              <td>{{score.score > 0 ? 'Correct' : 'Incorrect'}}</td>
            </tr>
          </table>

        </ng-container>

        <ng-container *ngIf="gameStatus?.gameMode === 'AWAITING_PLAYERS'">
          <h2>Waiting for players to join</h2>
        </ng-container>

        <ng-container *ngIf="gameStatus?.gameMode === 'NOT_RUNNING'">
          <h2>No GraphQL Quizzo game running</h2>
        </ng-container>

        <ng-container *ngIf="gameStatus?.gameMode === 'GAME_OVER'">
          <h2>Game over</h2>

          <table class="answerTable" *ngIf="gameStatus.finalScores">
            <tr>
              <th>Nickname</th>
              <th>Score</th>
            </tr>
            <tr *ngFor="let finalScore of gameStatus.finalScores">
              <td>{{finalScore.nickName}}</td>
              <td>{{finalScore.score}}</td>
            </tr>
          </table>
        </ng-container>

      </div>
    </div>
  `,
  styles: [
      `
      .jumbotron {
        background-color: #333E48;
        height: 100vh;
        overflow: scroll;
        padding-left: 12%;
        padding-right: 12%;
      }

      h2, .h2 {
        color: #fff;
        font-family: Helvetica;
      }

      p {
        color: #fff;
      }

      .display-2 {
        color: #fff;
        font-family: Helvetica;
        font-size: 80px;
        line-height: 100px;
        font-weight: bold;
        margin-top: 8px;
      }

      h2.display-2.ng-star-inserted {
        margin-bottom: 40px;
        color: #fff;
      }

      h3, .h3 {
        font-size: 30px;
        font-family: Helvetica;
        margin-top: 100px;
        list-style: none!important;
        color: #fff;
      }

      li.ng-star-inserted {
        list-style: none!important;
      }

      h4.display-4.ng-star-inserted {
        text-transform: uppercase;
        font-size: 20px;
        font-weight: bold;
        font-family: helvetica;
        color: rgb(4, 173, 105) !important;
      }

      table.answerTable {
        font-size: 1.7rem;
        color: #ffffff;
        border-collapse: collapse;
        border: none;
        width: 70vw;
        max-height:60vh;
        overflow: scroll;
      }

      table.answerTable tr, table.answerTable th, table.answerTable td {
        height: 20px !important;
      }
    `
  ]
})
export class DashboardComponent implements OnInit {
  constructor(private moderationService: ModerationService) { }
  gameStatus: GameStatus;
  subscription: Subscription;

  columnDefs = [
    {headerName: 'Nickname', field: 'nickName', width: 250, resizable: true},
    {headerName: 'Answer', field: 'anwerText', width: 800, resizable: true},
    {headerName: 'Correct', field: 'score', resizable: true}
    ];

  ngOnInit() {
      this.subscribe();
  }

  subscribe() {
    this.subscription = this.moderationService.gameStatusUpdates$
      .pipe(
        distinctUntilChanged()
      )
      .subscribe(
        (gameStatus: GameStatus) => {
          this.gameStatus = gameStatus;
        },
        (error) => {
          console.dir(error);
          alert(`error occurred: ${JSON.stringify(error)}`);
        },
        () => {
          console.log(`Websocket subscription ended.`);
          this.subscription = undefined;
        }
      );
  }

}
