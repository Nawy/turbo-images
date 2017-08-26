/**
 * Created by ermolaev on 5/9/17.
 */

import {Component} from '@angular/core'
import {UserInfo} from "../models/user-info.model";
import {UserService} from "../service/user.service";
import {Alert, AlertType} from "../models/alert.model";
import {
  UserChangeEmailForm,
  UserChangeNameForm,
  UserChangePasswordForm
} from "../models/forms/user-change-settings.model";
import {UserChangeField, UserChangePassword} from "../models/user-change-setting.model";
import {DANGER_INPUT, EMPTY_INPUT, InputTextForm} from "../models/input-text.model";
import {AuthorizationService} from "../service/authorization.service";
import {Router} from "@angular/router";


@Component({
  selector: "s-settings",
  templateUrl: './../templates/settings.template.html',
  styleUrls: ['./../css/settings.style.css']
})
export class SettingsComponent {

  alert: Alert;

  changeEmail: UserChangeEmailForm = {
    email: ""
  };

  changeName: UserChangeNameForm = {
    name: ""
  };

  changePassword: UserChangePasswordForm = {
    oldPassword: "",
    newPassword: "",
    newPasswordRepeat: ""
  };

  nameRegExp: RegExp = new RegExp("[a-zA-Z0-9]{3,20}");
  passwordRegExp: RegExp = new RegExp("[a-zA-Z0-9]{6,32}");
  emailRegExp: RegExp = new RegExp('^([a-zA-Z0-9]+\.?[a-zA-Z0-9]+)*\@{1,1}([a-zA-Z0-9]+\.?[a-zA-Z0-9]+)*$');

  nameSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  emailSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  passwordSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  repeatPasswordSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");

  userInfo: UserInfo = null;

  constructor(private userService: UserService, private authorizedService: AuthorizationService, private router: Router) {
    userService.userInfoSource.subscribe(userInfo => this.userInfo = userInfo);
    if (this.userInfo == null){
      this.userService.updateUserInfo();
    }
  }

  onChangePassword() {
    new Promise((resolve, reject) => {
      //correct password
      if (!this.passwordRegExp.test(this.changePassword.newPassword)) {
        this.passwordSettingForm.setValue(DANGER_INPUT, "Password is wrong!")
        reject(false)
      }

      //check similarity of passwords
      if (this.changePassword.newPassword != this.changePassword.newPasswordRepeat) {
        this.repeatPasswordSettingForm.setValue(DANGER_INPUT, "Password doesn't match!")
        reject(false)
      }
      resolve(true)
    }).then(res => {
      return this.userService.changePassword(
        new UserChangePassword(this.changePassword.oldPassword, this.changePassword.newPassword)
      )
    })
      .catch(res => this.alert = new Alert(AlertType.DANGER, res));
  }

  onChangeName() {
    let name = this.changeName.name

    new Promise((resolve, reject) => {
      //check correct name
      if (!this.nameRegExp.test(name)) {
        this.nameSettingForm.setValue(DANGER_INPUT, "Name is wrong!")
        reject(false)
      }
      resolve(true)
    })
      .then(res => this.authorizedService.isExistsNameOrEmail(name))
      .then(res => {
        if (res != false) {
          this.nameSettingForm.setValue(DANGER_INPUT, `${name} already exists!`);
          return Promise.reject(null)
        }

        return this.userService.changeName(
          new UserChangeField(name)
        )
      })
      .then(result => this.router.navigateByUrl("settings"))
      .catch(res => this.alert = new Alert(AlertType.DANGER, res));
  }

  onChangeEmail() {
    let email = this.changeEmail.email;

    new Promise((resolve, reject) => {
      //correct email
      if (email != "") {
        if (!this.emailRegExp.test(email)) {
          this.emailSettingForm.setValue(DANGER_INPUT, "Email is wrong!");
          reject(false)
        }
      }
      resolve(true)
    })
      .then(res => this.authorizedService.isExistsNameOrEmail(email))
      .then(res => {
        if (res != false) {
          this.emailSettingForm.setValue(DANGER_INPUT, `${email} already exists!`);
          return Promise.reject(null)
        }

        return this.userService.changeEmail(
          new UserChangeField(email)
        )
      })
      .catch(res => this.alert = new Alert(AlertType.DANGER, res));
  }

  checkFormPasswordsSimilarity(): boolean {
    if (this.changePassword.newPassword != this.changePassword.newPasswordRepeat) {
      this.repeatPasswordSettingForm.setValue(DANGER_INPUT, "Password doesn't match!")
      return false
    }
    this.repeatPasswordSettingForm.setValue(EMPTY_INPUT, "");
    return true
  }
}
