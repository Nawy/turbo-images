/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, OnInit} from '@angular/core'
import {Router} from '@angular/router';
import {UserService} from "../service/user.service";
import {UserInfo} from "../models/user-info.model";
import {AuthorizationService} from "../service/authorization.service";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {UploadModalComponent} from "./uploads/upload-modal.component";

@Component({
  selector: "s-navbar",
  templateUrl: './../templates/navbar.template.html',
  styleUrls: ['./../css/navbar.style.css']
})
export class NavbarComponent {

  userInfo : UserInfo;
  uploadModal : NgbModalRef;

  constructor(
    private userService : UserService,
    private authorizedService : AuthorizationService,
    private modalService: NgbModal,
    private router: Router
  ) {
    console.info("# navbar loaded!");
    this.userService.updateUserInfo();
    this.userService.userInfoSource.subscribe(
      userInfo => this.userInfo = userInfo
    )
  }

  addImage() {
    this.uploadModal = this.modalService.open(UploadModalComponent, { size: "lg" });
  }

  logout() {
    this.authorizedService.logout()
      .then(res => {
        this.router.navigateByUrl("signin");
      });
  }
}
