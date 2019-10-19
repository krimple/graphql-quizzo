export type Maybe<T> = T | null;

export interface NewUserData {
  nickName: string;

  email: string;

  phone: string;
}

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

  ping: boolean;

  me?: Maybe<string>;

  roles?: Maybe<string[]>;

  gameStatus: GameStatus;

  panelGameStatus: PanelGameStatus;
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

export interface GameStatus {
  gameMode: string;

  timeLeft?: Maybe<number>;

  gameTitle?: Maybe<string>;

  gameDescription?: Maybe<string>;

  currentQuestion?: Maybe<Question>;

  currentQuestionAnswered?: Maybe<boolean>;

  currentOptions?: Maybe<ChoiceOption[]>;

  questionScore?: Maybe<PlayerScoreReportEntry>;

  finalScore?: Maybe<number>;
}

export interface PlayerScoreReportEntry {
  gameTitle: string;

  questionTitle?: Maybe<string>;

  nickName: string;

  score: number;

  answer?: Maybe<string>;

  answerText?: Maybe<string>;
}

export interface PanelGameStatus {
  gameMode: string;

  timeLeft?: Maybe<number>;

  gameTitle?: Maybe<string>;

  gameDescription?: Maybe<string>;

  currentQuestion?: Maybe<Question>;

  currentQuestionAnswered?: Maybe<boolean>;

  currentOptions?: Maybe<ChoiceOption[]>;

  questionScores?: Maybe<PlayerScoreReportEntry[]>;

  finalScores?: Maybe<PlayerFinalScore[]>;
}

export interface PlayerFinalScore {
  nickName: string;

  score: number;
}

export interface Mutation {
  registerPlayer: string;

  registerModerator: string;

  registerAdmin: string;

  login?: Maybe<string>;

  selectGame: boolean;

  playerJoinCurrentGame: boolean;

  beginGamePlay: boolean;

  answerQuestion: boolean;

  endTurn: boolean;

  nextQuestion: boolean;

  reset: boolean;
}

export interface Player {
  nickName: string;
}

export interface User {
  id: string;

  roles: string[];
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
  details?: Maybe<NewUserData>;
}
export interface RegisterModeratorMutationArgs {
  id: string;
}
export interface RegisterAdminMutationArgs {
  id?: Maybe<string>;
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
