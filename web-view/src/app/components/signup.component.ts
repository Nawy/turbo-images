/**
 * Created by ermolaev on 5/5/17.
 */

import {Component} from "@angular/core";
import {Alert, AlertType} from "../models/alert.model";
import {AuthorizationService} from "../service/authorization.service";
import {UserSignup} from "../models/user-signup.model";
import {UserSignupForm} from "../models/user-signup-form.model";
import {Router} from "@angular/router";

@Component({
  templateUrl: './../templates/signup.template.html',
  styleUrls: ['./../css/signup.style.css']
})
export class SignupComponent {

  alert : Alert;
  signupData : UserSignupForm = {
    name : "",
    email : "",
    password : "",
    passwordRepeat : ""
  };

  constructor(private authorizedService : AuthorizationService, private router: Router) {
  }

  onCreateAccount() {
    this.authorizedService.signin(
      new UserSignup(
        this.signupData.name,
        this.signupData.password,
        this.signupData.email
      )
    ).then(
      (result : string) => {
        if(result != null) {
          this.alert = new Alert(AlertType.DANGER, result)
        } else {
          this.router.navigateByUrl("signin");
        }
      }
    );
  }
}
