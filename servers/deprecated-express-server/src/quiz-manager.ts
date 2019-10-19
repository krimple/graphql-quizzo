import fs from 'fs';
import {Store} from 'redux';
import store from './store/store';
import {Player} from '../../codegen/src/generated/graphql';

export class QuizManager {
  public static instance(): QuizManager {
    if (!QuizManager.quizManagerInstance) {
      QuizManager.quizManagerInstance = new QuizManager();
    }
    return QuizManager.quizManagerInstance;
  }

  private static quizManagerInstance: QuizManager;
  private quizData: any;

  private constructor() {
    this.quizData = {};
  }

  public get = (id: number | null = null) => {
    if (id === null) {
      return this.quizData.quizzes;
    } else {
      return this.quizData.quizzes.find((q) => q.id === id) || null;
    }
  };

  public tokenAssignedToPlayer(token): Player | null {
    const players = store.getState().players;
    const playerIndexByToken = players.indexOf((p) => p.token === token);
    if (playerIndexByToken > -1) {
      return players[playerIndexByToken];
    } else {
      return null;
    }
  }
}
