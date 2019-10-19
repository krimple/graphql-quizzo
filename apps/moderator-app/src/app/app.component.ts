import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <p-toast></p-toast>
    <h1>Welcome, Quizzo Moderator</h1>
    <hr/>
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
}
