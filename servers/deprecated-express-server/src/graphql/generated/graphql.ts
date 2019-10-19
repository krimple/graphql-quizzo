export type Maybe<T> = T | null;

export interface SignInCredentials {
  userName: string;

  password: string;
}

// ====================================================
// Types
// ====================================================

export interface Query {
  getQuizzes?: Maybe<(Maybe<Quiz>)[]>;

  getQuiz: Quiz;

  me?: Maybe<User>;

  roles?: Maybe<string[]>;

  gameStatus: GameStatus;
}

export interface Quiz {
  id: number;

  title: string;

  description: string;

  questions: Question[];
}

export interface Question {
  text: string;

  options: ChoiceOption[];
}

export interface ChoiceOption {
  key: string;

  label: string;
}

export interface User {
  id: string;

  roles: string[];
}

export interface GameStatus {
  gameMode: string;

  timeLeft?: Maybe<number>;

  currentQuestion?: Maybe<Question>;
}

export interface Mutation {
  registerPlayer: RegistrationResponse;

  registerUser: RegistrationResponse;

  login?: Maybe<string>;

  selectGame: boolean;

  beginGamePlay: boolean;

  answerQuestion: boolean;

  endTurn: boolean;

  nextQuestion: boolean;

  sendPing: string;

  reset: boolean;
}

export interface RegistrationResponse {
  nickName?: Maybe<string>;

  token?: Maybe<string>;
}

export interface Player {
  nickName: string;
}

export interface SignInResponse {
  user: User;

  token: string;
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
export interface RegisterUserMutationArgs {
  id: string;

  password: string;
}
export interface LoginMutationArgs {
  credentials?: Maybe<SignInCredentials>;
}
export interface SelectGameMutationArgs {
  quizId: number;
}
export interface AnswerQuestionMutationArgs {
  answer: string;
}
