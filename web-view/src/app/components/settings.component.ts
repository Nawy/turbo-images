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
import {INVALID_INPUT, EMPTY_INPUT, InputTextForm, VALID_INPUT} from "../models/input-text.model";
import {AuthorizationService} from "../service/authorization.service";
import {isUndefined} from "util";


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
  oldPasswordSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  passwordSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");
  repeatPasswordSettingForm: InputTextForm = new InputTextForm(EMPTY_INPUT, "");

  userInfo: UserInfo = null;

  constructor(private userService: UserService, private authorizedService: AuthorizationService) {
    userService.userInfoSource.subscribe(userInfo => {
      this.userInfo = userInfo;
      this.changeEmail.email = this.userInfo.email;
      this.changeName.name = this.userInfo.name;
    });
    if (this.userInfo == null) {
      this.userService.updateUserInfo();
    }
  }

  onChangePassword() {
    new Promise((resolve, reject) => {

      //old password
      if (this.changePassword.oldPassword == null || this.changePassword.oldPassword == '') {
        this.oldPasswordSettingForm.setValue(INVALID_INPUT, "Password is empty!");
        return reject()
      }
      this.oldPasswordSettingForm.setValue(EMPTY_INPUT, "");

      //new password
      if (!this.passwordRegExp.test(this.changePassword.newPassword)) {
        this.passwordSettingForm.setValue(INVALID_INPUT, "Password must have min 6 symbols!");
        return reject()
      }
      this.passwordSettingForm.setValue(EMPTY_INPUT, "");

      //check similarity of passwords
      if (this.changePassword.newPassword != this.changePassword.newPasswordRepeat) {
        this.repeatPasswordSettingForm.setValue(INVALID_INPUT, "Password doesn't match!");
        return reject()
      }
      this.repeatPasswordSettingForm.setValue(EMPTY_INPUT, "");

      return resolve()
    }).then(() => {
      return this.userService.changePassword(
        new UserChangePassword(this.changePassword.oldPassword, this.changePassword.newPassword)
      )
    }).then(() => this.alert = new Alert(AlertType.SUCCESS, "SUCCESS"))
      .catch(res => {
        if (res == "403") {
          this.alert = new Alert(AlertType.DANGER, "Incorrect old password");
          return
        }
        if (!isUndefined(res)) {
          this.alert = new Alert(AlertType.DANGER, res);
        }
      })
  }

  onChangeName() {
    let name = this.changeName.name;

    new Promise((resolve, reject) => {

      //check correct name
      if (!this.nameRegExp.test(name)) {
        this.nameSettingForm.setValue(INVALID_INPUT, "Name is wrong!");
        return reject()
      }

      return resolve()
    })
      .then(() => this.authorizedService.isExistsNameOrEmail(name))
      .then(nameExists => {
        if (nameExists == true) {
          this.nameSettingForm.setValue(INVALID_INPUT, `${name} already exists!`);
          return Promise.reject(null)
        }
        this.nameSettingForm.setValue(EMPTY_INPUT, "");

        return this.userService.changeName(
          new UserChangeField(name)
        )
      })
      .then(() => this.nameSettingForm.setValue(VALID_INPUT, "Name changed"))
      .catch(() => this.nameSettingForm.setValue(INVALID_INPUT, "Internal error"));
  }

  onChangeEmail() {
    let email = this.changeEmail.email;

    new Promise((resolve, reject) => {


      //correct email
      if (email == null || email == "") {
        this.emailSettingForm.setValue(INVALID_INPUT, "Email is empty!");
        return reject()
      }
      if (!this.emailRegExp.test(email)) {
        this.emailSettingForm.setValue(INVALID_INPUT, "Incorrect email!");
        return reject()
      }

      return resolve()
    })
      .then(() => this.authorizedService.isExistsNameOrEmail(email))
      .then(res => {
        if (res != false) {
          this.emailSettingForm.setValue(INVALID_INPUT, `${email} already exists!`);
          return Promise.reject(null)
        }

        return this.userService.changeEmail(
          new UserChangeField(email)
        )
      })
      .then(() => this.emailSettingForm.setValue(VALID_INPUT, "Email changed"))
      .catch(() => this.emailSettingForm.setValue(INVALID_INPUT, "Internal error"));
  }

}
