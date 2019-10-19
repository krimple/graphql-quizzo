import {ApolloClient} from 'apollo-client';
import {InMemoryCache} from 'apollo-cache-inmemory';
import {HttpLink} from 'apollo-link-http';
import {onError} from 'apollo-link-error';
import {ApolloLink} from 'apollo-link';
import {setContext} from 'apollo-link-context';

const httpGraphQLRoot = `graphql`;

const httpLink = new HttpLink({
    uri: httpGraphQLRoot,
    credentials: 'same-origin'
});

// nb : from https://www.apollographql.com/docs/react/recipes/authentication.html
const authLink = setContext((_, {headers}) => {
    // get the authentication token from local storage if it exists
    const token = localStorage.getItem('token');
    // return the headers to the context so httpLink can read them
    return {
        headers: {
            ...headers,
            authorization: token ? `Bearer ${token}` : "",
        }
    };
});

const errorLink = onError(({graphQLErrors, networkError}) => {
    if (graphQLErrors) {
        graphQLErrors.map(({message, locations, path}) =>
            console.log(
                `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`,
            )
        );
    }
    if (networkError) {
        switch (networkError.statusCode) {
            case 401:
                // bad JWT token, need to remove...
                // TODO - dupes
                localStorage.removeItem('token');
                // TODO - show login
                break;
            case 403:
                // not authorized, no token
                localStorage.removeItem('token');
               // TODO show login dialog
                break;
            default:
                console.log('Unexpected network error', networkError);
        }
    }
});

const link = ApolloLink.from([
    authLink,
    errorLink,
    httpLink
]);

export const client = new ApolloClient({
    link,
    cache: new InMemoryCache()
});
