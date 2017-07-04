/**
 * Created by ermolaev on 5/5/17.
 */

import {Component} from "@angular/core";
import {Alert, AlertType} from "../models/alert.model";
import {AuthorizationService} from "../service/authorization.service";
import {UserSignup} from "../models/user-signup.model";
import {UserSignupForm} from "../models/forms/user-signup-form.model";
import {Router} from "@angular/router";
import {InputTextForm, EMPTY_INPUT, DANGER_INPUT} from "../models/input-text.model";

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

  nameRegExp : RegExp = new RegExp("[a-zA-Z0-9]{3,20}");
  passwordRegExp : RegExp = new RegExp("[a-zA-Z0-9]{6,32}");
  emailRegExp : RegExp = new RegExp('^([a-zA-Z0-9]+\.?[a-zA-Z0-9]+)*\@{1,1}([a-zA-Z0-9]+\.?[a-zA-Z0-9]+)*$');
  emailRegExp2 : RegExp = new RegExp('/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/');

  nameForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  emailForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  passwordForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  repeatPasswordForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");

  constructor(private authorizedService : AuthorizationService, private router: Router) {}

  onSignup() {
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
    if (!this.checkName()) {
      return false
    }

    if (!this.checkPassword()) {
      return false
    }

    if(this.signupData.email != "") {
      if (!this.checkEmail()) {
        return false
      }
    }
    return true
  }

  checkName() : boolean {
    if (!this.nameRegExp.test(this.signupData.name)) {
      this.nameForm.setValue(DANGER_INPUT, "Name is wrong!")
      return false
    }

    this.nameForm.setValue(EMPTY_INPUT, "");
    return true
  }

  checkEmail() : boolean {
    if(this.signupData.email != "") {
      if (!this.emailRegExp2.test(this.signupData.email)) {
        this.emailForm.setValue(DANGER_INPUT, "Email is wrong!")
        return false
      }
    }

    this.emailForm.setValue(EMPTY_INPUT, "");
    return true
  }

  checkPassword() : boolean {
    if(!this.passwordRegExp.test(this.signupData.password)) {
      this.passwordForm.setValue(DANGER_INPUT, "Password is wrong!")
      return false
    }

    this.passwordForm.setValue(EMPTY_INPUT, "");
    return this.checkPasswordsSimilarity()
  }

  checkPasswordsSimilarity() : boolean {
    if(this.signupData.password != this.signupData.passwordRepeat) {
      this.repeatPasswordForm.setValue(DANGER_INPUT, "Password doesn't match!")
      return false
    }
    this.repeatPasswordForm.setValue(EMPTY_INPUT, "");
    return true
  }
}
