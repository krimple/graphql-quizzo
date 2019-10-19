import * as actions from '../actions';
import {loginMutation, joinGameMutation } from '../../graphql';

export function signOnToQuizzoSystem(nickName) {
   return function(dispatch) {
      return new Promise(async (resolve, reject) => {
          try {
             dispatch({
                type: actions.ACTION_SIGN_ON_BEGIN,
                nickName
             });
             const token = await loginMutation(nickName);
             localStorage.setItem('token', token);
             dispatch({
                type: actions.ACTION_SIGN_ON_SUCCESS,
                token: token,
                nickName
             });
             resolve();
          } catch (e) {
              localStorage.removeItem('token');
              console.log(`Failure in signing on. ${JSON.stringify(e)}`);
              dispatch({
                 type: actions.ACTION_SIGN_ON_FAILURE
              });
              reject();
          }
      })

   }
}

export function joinGame() {
   return async function(dispatch) {
      try {
         dispatch({
            type: actions.ACTION_JOIN_GAME_BEGIN
         });
         await joinGameMutation();
         dispatch({
            type: actions.ACTION_JOIN_GAME_SUCCESS
         });
      } catch (e) {
         console.log(`Failure in joining game. ${JSON.stringify(e)}`);
         dispatch({
            type: actions.ACTION_JOIN_GAME_FAILURE
         });
      }
   }
}


