/**
 * Created by ermolaev on 5/10/17.
 */
import {Component} from '@angular/core'
import {Router} from "@angular/router";
import {AuthorizationService} from "../service/authorization.service";
import {UserSignin} from "../models/user-signin.model";
import {Alert, AlertType} from "../models/alert.model";
import {UserSigninForm} from "../models/forms/user-signin-form.model";

@Component({
  selector: "s-login",
  templateUrl: './../templates/signin.template.html',
  styleUrls: ['./../css/signin.style.css']
})
export class SigninComponent {

  alert : Alert;
  signinData : UserSigninForm = {
    email : "",
    password : ""
  };

  constructor(private authorizedService : AuthorizationService, private router: Router) {}

  onSignin() {
    this.authorizedService.signin(
      new UserSignin(
        this.signinData.email,
        this.signinData.password
      )
    ).then(
      (result : string) => {
        if(result != null) {
          this.alert = new Alert(AlertType.DANGER, result)
        } else {
          this.router.navigateByUrl("/");
        }
      }
    )
  }
}
