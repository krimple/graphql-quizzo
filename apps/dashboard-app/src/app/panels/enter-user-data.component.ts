import {Component, OnInit} from '@angular/core';
import {GameplayService} from '../services/gameplay.service';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';

// TODO - convert to reactive form and add validation
@Component({
  template: `
    <div class="container">
      <h3>Login to Administer Gameplay</h3>
      <form (ngSubmit)="login()">
        <div class="form-group">
          <label for="id">ID</label>:
          <input class="form-control" type="text" required name="id" [(ngModel)]="form.id" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input class="form-control" type="password" required name="password" [(ngModel)]="form.password" />
          <span class="error" *ngIf="invalidPassword">Password invalid.</span>
        </div>
        <button class="btn btn-primary" type="submit">Click to send</button>
      </form>
    </div>
  `,

  styles: [
    `
      h3, .h3 {
        font-size: 30px;
        margin-top: 100px;
        margin-bottom: 40px;
        font-weight: bold;
      }
      
      input {
        font-size: 30px;
      }
      
      label {
        font-size: 30px;
      }

      button:not(:disabled), [type="button"]:not(:disabled), [type="reset"]:not(:disabled), [type="submit"]:not(:disabled) {
        background-color: #04AD68;
        border: none;
        padding: 30px 60px;
        color: #fff;
        font-weight: bold;
      text-transform: uppercase;
      font-size: 20px;
      }
    `,
  ]
})
export class EnterUserDataComponent implements OnInit {

  invalidPassword = false;

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit() {
    if (localStorage.getItem("token")) {
      this.router.navigate(['/']);
    }

  }

  form = {
    id: 'krimple',
    password: ''
  };

  async login() {
    try {
      const userId = await this.authService.login(this.form.id, this.form.password);
      // not necessary since we're routing, but heck...
      this.invalidPassword = false;
      if (userId) {
        this.router.navigate(['/']);
      }
    } catch (e) {
      this.invalidPassword = true;
    }
  }
}
