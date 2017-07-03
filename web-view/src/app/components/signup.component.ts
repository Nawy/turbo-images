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

  nameRegExp : RegExp = new RegExp("[a-zA-Z0-9]{1,20}");
  passwordRegExp : RegExp = new RegExp("[a-zA-Z0-9]{6,32}")

  constructor(private authorizedService : AuthorizationService, private router: Router) {
  }

  onCreateAccount() {
    if(this.isCorrectForm()) {
      this.authorizedService.signup(
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

  isCorrectForm() : boolean {
    if (!this.nameRegExp.test(this.signupData.name)) {
      this.alert = new Alert(AlertType.DANGER, "Name is wrong!");
      return false
    }

    if(!this.passwordRegExp.test(this.signupData.password)) {
      this.alert = new Alert(AlertType.DANGER, "Password is wrong!");
      return false
    }

    if(this.signupData.password != this.signupData.passwordRepeat) {
      this.alert = new Alert(AlertType.DANGER, "Password doesn't match!");
      return false
    }
    return true
  }
}
