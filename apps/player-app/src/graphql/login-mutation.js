import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function loginMutation(nickName) {
    return new Promise((resolve, reject) => {
        try {
            client.mutate({
                mutation: gql`
                  mutation login($credentials: SignInCredentials){
                       login(credentials: $credentials)
                  }`,
                variables: {
                    credentials: {
                        userName: nickName,
                        // TODO - extract and maybe randomize this/pass it in at runtime?
                        password: 'abc123'
                    }
                }
            })
                .then(
                    (result) => resolve(result.data.login),
                    (error) => reject(error)
                );
        } catch (e) {
            reject(e);
        }
        });
}
