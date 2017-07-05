/**
 * Created by ermolaev on 5/10/17.
 */
import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AuthorizationService} from "../service/authorization.service";
import {UserSignin} from "../models/user-signin.model";
import {Alert} from "../models/alert.model";
import {UserSigninForm} from "../models/forms/user-signin-form.model";
import {UserService} from "../service/user.service";

@Component({
  selector: "s-login",
  templateUrl: './../templates/signin.template.html',
  styleUrls: ['./../css/signin.style.css']
})
export class SigninComponent implements OnInit {

  alert : Alert;
  signinData : UserSigninForm = {
    email : "",
    password : ""
  };

  constructor(private authorizedService : AuthorizationService, private userService : UserService, private router: Router) {}

  ngOnInit() {
    if(this.userService.userInfo != null) {
      this.router.navigateByUrl("/");
    }
  }

  onSignin() {
    this.authorizedService.signin(
      new UserSignin(
        this.signinData.email,
        this.signinData.password
      )
    ).then(res => {
      return this.userService.getUserInfo()
    }).then(userInfo => {
      this.router.navigateByUrl("/");
    });
  }
}
