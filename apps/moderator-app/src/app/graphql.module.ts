import {NgModule} from '@angular/core';
import {environment} from '../environments/environment';
import {ApolloModule, APOLLO_OPTIONS, Apollo} from 'apollo-angular';
import {HttpLinkModule, HttpLink} from 'apollo-angular-link-http';
import { split } from 'apollo-link';
import { WebSocketLink } from 'apollo-link-ws';
import { InMemoryCache} from 'apollo-cache-inmemory';
import { getMainDefinition } from 'apollo-utilities';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {GameplayService} from './services/gameplay.service';

@NgModule({
  exports: [ApolloModule, HttpLinkModule, HttpClientModule],
  providers: [GameplayService]
})
export class GraphQLModule {
 constructor(apollo: Apollo, private httpClient: HttpClient) {
   console.log(`HTTP environment is ${environment.uris.http}`);
   const link = new HttpLink(httpClient).create({
     uri: environment.uris.http
   });
   const cache = new InMemoryCache({
      resultCaching: false
   });
   apollo.create({
     link,
     cache
   });
 }
}
