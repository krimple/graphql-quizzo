import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GraphQLModule } from './graphql.module';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { EnterUserDataComponent } from './panels/enter-user-data.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {JwtAuthguardService} from './services/jwt-authguard.service';
import {ForceAuthInterceptor} from './services/force-auth-interceptor';
import {SendTokenInterceptor} from './services/send-token-interceptor';
import {ComponentIntegrationModule} from './component-integration.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DashboardComponent} from './panels/dashboard.component';
import {AgGridModule} from 'ag-grid-angular';

@NgModule({
  declarations: [
    AppComponent,
    EnterUserDataComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    GraphQLModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    ComponentIntegrationModule,
    AgGridModule,
  ],
  exports: [
    EnterUserDataComponent,
    DashboardComponent
  ],
  providers: [
    JwtAuthguardService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: SendTokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ForceAuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
