import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {EnterUserDataComponent} from './panels/enter-user-data.component';
import { JwtAuthguardService } from './services/jwt-authguard.service';
import {GameModeratorComponent} from './panels/game-moderator.component';

const routes: Routes = [
  { path: '',  pathMatch: 'full', redirectTo: 'moderator' },
  { path: 'moderator', component: GameModeratorComponent, canActivate: [JwtAuthguardService] },
  { path: 'login', component: EnterUserDataComponent },
  { path: '**', redirectTo: '/moderator' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    useHash: true
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
