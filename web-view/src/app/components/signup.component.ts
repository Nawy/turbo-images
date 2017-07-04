/**
 * Created by ermolaev on 5/5/17.
 */

import {Component} from "@angular/core";
import {Alert, AlertType} from "../models/alert.model";
import {AuthorizationService} from "../service/authorization.service";
import {UserSignup} from "../models/user-signup.model";
import {UserSignupForm} from "../models/forms/user-signup-form.model";
import {Router} from "@angular/router";
import {DANGER_INPUT, EMPTY_INPUT, InputTextForm} from "../models/input-text.model";

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
  emailRegExp2 : RegExp = new RegExp(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/);

  nameForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  emailForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  passwordForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  repeatPasswordForm : InputTextForm = new InputTextForm(EMPTY_INPUT, "");

  constructor(private authorizedService : AuthorizationService, private router: Router) {}

  onSignup() {

    const promise = new Promise((resolve, reject) => {
      //check correct name
      if (!this.nameRegExp.test(this.signupData.name)) {
        this.nameForm.setValue(DANGER_INPUT, "Name is wrong!")
        reject(false)
      }
      //correct email
      if(this.signupData.email != "") {
        if (!this.emailRegExp.test(this.signupData.email)) {
          this.emailForm.setValue(DANGER_INPUT, "Email is wrong!")
          reject(false)
        }
      }
      //correct password
      if(!this.passwordRegExp.test(this.signupData.password)) {
        this.passwordForm.setValue(DANGER_INPUT, "Password is wrong!")
        reject(false)
      }

      //check similarity of passwords
      if(this.signupData.password != this.signupData.passwordRepeat) {
        this.repeatPasswordForm.setValue(DANGER_INPUT, "Password doesn't match!")
        reject(false)
      }
      resolve(true)
    }).then(res => {
        return this.authorizedService.isExistsNameOrEmail(this.signupData.name);
    }).then(res => {
      if(res != false) {
        this.nameForm.setValue(DANGER_INPUT, `${this.signupData.name} already exists!`);
        return Promise.reject(null)
      }
      return this.authorizedService.isExistsNameOrEmail(this.signupData.email);
    }).then(res => {
      if(res != false) {
        this.emailForm.setValue(DANGER_INPUT, `${this.signupData.email} already exists!`);
        return Promise.reject(null)
      }

      return this.authorizedService.signup(
        new UserSignup(
          this.signupData.name,
          this.signupData.password,
          this.signupData.email
        )
      );
    }).then(
      (result : string) => {
        if(result != null) {
          this.alert = new Alert(AlertType.DANGER, result)
        } else {
          this.router.navigateByUrl("signin");
        }
      }
    );
  }

  checkFormName() {
    if (!this.nameRegExp.test(this.signupData.name)) {
      this.nameForm.setValue(DANGER_INPUT, "Name is wrong!")
      return false
    }

    this.authorizedService
      .isExistsNameOrEmail(this.signupData.name)
      .then(res => {
        if(res) {
          this.nameForm.setValue(DANGER_INPUT, `${this.signupData.name} already exists!`);
          return false;
        } else {
          this.nameForm.setValue(EMPTY_INPUT, "");
        }
      });

    return true
  }

  checkFormEmail() {
    if(this.signupData.email == null || this.signupData.email == "") {
      return false
    }
    if (!this.emailRegExp2.test(this.signupData.email)) {
      this.emailForm.setValue(DANGER_INPUT, "Email is wrong!")
      return false
    }
    this.authorizedService
      .isExistsNameOrEmail(this.signupData.email)
      .then(res => {
        if(res) {
          this.emailForm.setValue(DANGER_INPUT, `${this.signupData.email} already exists!`);
          return false;
        } else {
          this.emailForm.setValue(EMPTY_INPUT, "");
        }
      });

    return true
  }

  checkFormPassword() {
    if(!this.passwordRegExp.test(this.signupData.password)) {
      this.passwordForm.setValue(DANGER_INPUT, "Password is wrong!")
      return false
    }

    this.passwordForm.setValue(EMPTY_INPUT, "");
    return this.checkFormPasswordsSimilarity()
  }

  checkFormPasswordsSimilarity() : boolean {
    if(this.signupData.password != this.signupData.passwordRepeat) {
      this.repeatPasswordForm.setValue(DANGER_INPUT, "Password doesn't match!")
      return false
    }
    this.repeatPasswordForm.setValue(EMPTY_INPUT, "");
    return true
  }
}
