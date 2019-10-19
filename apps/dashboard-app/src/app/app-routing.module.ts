import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {EnterUserDataComponent} from './panels/enter-user-data.component';
import { JwtAuthguardService } from './services/jwt-authguard.service';
import {DashboardComponent} from "./panels/dashboard.component";

const routes: Routes = [
  { path: '',  pathMatch: 'full', redirectTo: 'dashboard' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [JwtAuthguardService] },
  { path: 'login', component: EnterUserDataComponent },
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    useHash: true
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
