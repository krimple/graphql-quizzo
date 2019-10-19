export type Maybe<T> = T | null;

export interface QuestionAnswerInput {
  playerId: string;

  answer: string;
}

export enum ChoiceEnum {
  A = "A",
  B = "B",
  C = "C",
  D = "D"
}

// ====================================================
// Interfaces
// ====================================================

export interface QuestionData {
  id: number;

  text: string;
}

// ====================================================
// Types
// ====================================================

export interface Query {
  me?: Maybe<User>;

  getQuizzes?: Maybe<(Maybe<Quiz>)[]>;

  getQuiz: Quiz;
}

export interface User {
  id: string;

  username: string;
}

export interface Quiz {
  id: number;

  name: string;

  description: string;

  questions: Question[];
}

export interface MultipleChoiceQuestion extends QuestionData {
  id: number;

  text: string;

  options: ChoiceOption[];
}

export interface ChoiceOption {
  key: ChoiceEnum;

  label: string;
}

export interface TrueFalseQuestion extends QuestionData {
  id: number;

  text: string;
}

export interface FillInBlankQuestion extends QuestionData {
  id: number;

  text: string;
}

export interface Mutation {
  registerPlayer?: Maybe<RegistrationResponse>;

  answerQuestion?: Maybe<boolean>;

  startGame?: Maybe<boolean>;
}

export interface RegistrationResponse {
  nickName?: Maybe<string>;

  token?: Maybe<string>;
}

export interface Subscription {
  nextMessage?: Maybe<GamePlayMessage>;
}

export interface TickTock {
  timeLeft?: Maybe<number>;
}

export interface SubmitQuestion {
  question: Question;
}

export interface GameStart {
  gameId: number;

  gameToken: string;

  otherPlayers?: Maybe<Player[]>;
}

export interface Player {
  playerId: number;

  nickName?: Maybe<string>;
}

export interface TurnOver {
  winningPlayerId: number;

  winningPlayerScore: number;

  leaderBoard?: Maybe<LeaderboardEntry[]>;

  yourAnswer: string;

  yourAnswerCorrect: boolean;

  yourScore: number;
}

export interface LeaderboardEntry {
  playerId: number;

  nickName: string;

  score: number;
}

export interface GameOver {
  finalScores?: Maybe<LeaderboardEntry[]>;
}

// ====================================================
// Arguments
// ====================================================

export interface GetQuizQueryArgs {
  id: number;
}
export interface RegisterPlayerMutationArgs {
  nickName: string;

  password: string;
}
export interface AnswerQuestionMutationArgs {
  input: QuestionAnswerInput;
}
export interface StartGameMutationArgs {
  quizId: number;
}

// ====================================================
// Unions
// ====================================================

export type Question =
  | MultipleChoiceQuestion
  | TrueFalseQuestion
  | FillInBlankQuestion;

export type GamePlayMessage =
  | TickTock
  | SubmitQuestion
  | GameStart
  | TurnOver
  | GameOver;
