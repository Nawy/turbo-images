/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core'
import { Router } from '@angular/router';
import {UserService} from "../service/user.service";
import {UserInfo} from "../models/user-info.model";
import {AuthorizationService} from "../service/authorization.service";

@Component({
  selector: "s-navbar",
  templateUrl: './../templates/navbar.template.html',
  styleUrls: ['./../css/navbar.style.css']
})
export class NavbarComponent {

  constructor(private userService : UserService, private authorizedService : AuthorizationService, private router: Router) {
    userService.getUserInfo();
  }

  logout() {
    this.authorizedService.logout()
      .then(res => {
        this.userService.userInfo = null;
        this.router.navigateByUrl("signin");
      });
  }
}
