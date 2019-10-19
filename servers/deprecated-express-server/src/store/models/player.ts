import {User} from "../../graphql/generated/graphql";

export interface Player {
  nickName: string;
  score?: number;
  success?: boolean;
}
