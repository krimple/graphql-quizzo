import {Component, OnInit, ViewChild} from "@angular/core";
import {Form, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ModerationService} from "../services/moderation.service";
import {MessageService} from "primeng/api";

@Component({
  selector: 'register-player',
  template: `
    <div class="container">
      <h3 class="text-center">Register a new Player</h3>

      <form class="form" [formGroup]="form" (ngSubmit)="registerPlayer()">

        <div class="form-group">
          <label for="id">Player UserName/NickName</label>
          <input class="form-control" formControlName="nickName">
        </div>

        <div class="form-group">
          <label for="email">Email Address</label>
          <input class="form-control" formControlName="email">
        </div>


        <div class="form-group">
          <label for="phone">Phone Number</label>
          <input class="form-control" formControlName="phone">
        </div>

        <button class="btn btn-lg btn-primary float-right" [disabled]="form.invalid === true">
          Create User!
        </button>
      </form>
    </div>
  `,
  styles: [
    `
      button:not(:disabled), [type="button"]:not(:disabled), [type="reset"]:not(:disabled), [type="submit"]:not(:disabled) {
        background-color: #04AD69!important;
        padding: 30px 60px;
        font-weight: bold;
        text-transform: uppercase;
        border-radius: 0px;
        border: none;
      }
      button.btn.btn-lg.btn-block.btn-primary.ng-star-inserted {
        background-color: #04AD69!important;
        padding: 30px 60px;
        font-weight: bold;
        text-transform: uppercase;
        border-radius: 0px;
        border: none;
      }
      
      h3.text-center {
        font-size: 30px;
        font-weight: bold;
        margin: 80px 0 40px 0;
      }

      label {
        font-size: 20px;
        margin-bottom: 20px;
      }

      input.form-control {
        font-size: 20px;
      }
    `
  ]
})
export class RegisterPlayerComponent implements OnInit {
  scanning = false;
  form: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private moderationService: ModerationService,
              private messageService: MessageService) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      "nickName": [''],
      "email": ['', Validators.email],
      "phone": ['' ]
    });
  }

  registerPlayer() {
    if (this.form.valid) {
      const formData = this.form.value;
      this.moderationService.addPlayer(formData.nickName, formData.email, formData.phone)
        .subscribe(
          () => {
            this.messageService.add({key: "User Created", detail: `User ${formData.nickName} created.`});
            this.form.reset();
          },
          (e) => {
            this.messageService.add({key: 'Player not created.', detail: JSON.stringify(e), severity: 'error', life: 60000, closable: true});
          }
        )

    }
  }
}
