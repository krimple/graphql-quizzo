import {Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {GameplayService} from './services/gameplay.service';



@Component({
  selector: 'app-root',
  template: `
    <p-toast></p-toast>
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
}
