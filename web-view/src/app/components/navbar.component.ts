/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core'
import { Router } from '@angular/router';
import {UserService} from "../service/user.service";
import {UserInfo} from "../models/user-info.model";
import {AuthorizationService} from "../service/authorization.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ImageService} from "../service/image.service";

@Component({
  selector: "s-navbar",
  templateUrl: './../templates/navbar.template.html',
  styleUrls: ['./../css/navbar.style.css']
})
export class NavbarComponent {

  userInfo : UserInfo;
  imagePath : string;

  constructor(
    private userService : UserService,
    private authorizedService : AuthorizationService,
    private modalService: NgbModal,
    private imageService: ImageService,
    private router: Router
  ) {
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

  dropImage(event : any) {
    event.preventDefault();
    event.stopPropagation();
    this.imagePath = URL.createObjectURL(event.dataTransfer.files[0]);
    this.imageService.uploadImage(event.dataTransfer.files[0]);
    //content.close();
  }

  dragoverImage(event : any) {
    event.preventDefault();
    event.stopPropagation();
    console.info("DRAGG OVER");
  }

  dragoverTest(event : any) {
    event.preventDefault();
    event.stopPropagation();
    console.info("TEST DRAGG OVER");
  }
}
