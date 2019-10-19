import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

// Picks current user from Spring security context and JWT
export function joinGameMutation() {
    return new Promise((resolve, reject) => {
        try {
            client.mutate({
                mutation: gql`
                  mutation playerJoinCurrentGame {
                       playerJoinCurrentGame
                  }`
            })
                .then(
                    (result) => {
                        resolve(result.data.playerJoinCurrentGame);
                    },
                    (error) => reject(error)
                );
        } catch (e) {
            reject(e);
        }
    });
}