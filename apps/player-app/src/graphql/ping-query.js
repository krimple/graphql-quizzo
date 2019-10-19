import {client} from './apollo-graphql-client';
import gql from 'graphql-tag';

export function pingQuery() {
    return client.query({
        fetchPolicy: 'no-cache',
        query: gql`
            query {
              ping
            }`
    });
}