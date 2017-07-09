/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core'
import { Router } from '@angular/router';
import {UserService} from "../service/user.service";
import {UserInfo} from "../models/user-info.model";
import {AuthorizationService} from "../service/authorization.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "s-navbar",
  templateUrl: './../templates/navbar.template.html',
  styleUrls: ['./../css/navbar.style.css']
})
export class NavbarComponent {

  userInfo : UserInfo;
  constructor(private userService : UserService, private authorizedService : AuthorizationService, private modalService: NgbModal, private router: Router) {
    this.userService.updateUserInfo();
    userService.userInfoSource.subscribe(
      userInfo => this.userInfo = userInfo
    )
  }

  addImage(content) {
    this.modalService.open(content, { windowClass: 'dark-modal' });
  }

  logout() {
    this.authorizedService.logout()
      .then(res => {
        this.router.navigateByUrl("signin");
      });
  }

  dropImage(event : any, content : any) {
    event.preventDefault();
    console.info(content);

    //content.close();
    console.info("UPLOOOOOAAAD!");
    console.info(event.dataTransfer.files[0]);
    console.info(event.dataTransfer.getData());
  }

  dropoverImage(event : any, content : any) {
    event.preventDefault();
    console.info("DRAGG OVER");
  }
}
