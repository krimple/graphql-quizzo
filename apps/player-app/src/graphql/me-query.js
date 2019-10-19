import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function meQuery() {
    return client.query({
        fetchPolicy: 'no-cache',
        query: gql`
            query {
             me 
            }`
    });
}