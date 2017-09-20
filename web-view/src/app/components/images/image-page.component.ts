import {Component, OnInit} from "@angular/core";
import {PersonalHolderService} from "../../service/personal-holder.service";
import {UserImage} from "../../models/user-image.model";
import {ActivatedRoute} from "@angular/router";
import {ImageService} from "../../service/image.service";
import {Location} from '@angular/common';
import {environment} from "../../../environments/environment";
import {UserInfo} from "../../models/user-info.model";
import {UserService} from "app/service/user.service";
import {Subject} from "rxjs/Subject";
import {Observable} from "rxjs/Observable";
import {Scheduler} from "rxjs/Scheduler";

/**
 * Created by ermolaev on 7/23/17.
 */

@Component({
  templateUrl: './../../templates/images/image-page.template.html',
  styleUrls: ['./../../css/ImagePageComponent.style.css']
})
export class ImagePageComponent implements OnInit {

  private titleObserver = new Subject<string>();
  private descriptionObserver = new Subject<string>();
  userInfo: UserInfo = null;

  userImage: UserImage;
  imageSource: string;
  creationDate: string;

  constructor(private route: ActivatedRoute,
              private imageService: ImageService,
              private location: Location,
              private personalHolderService: PersonalHolderService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.userService.userInfoSource.subscribe(userInfo => {
      this.userInfo = userInfo;
    });
    if (this.userInfo == null) {
      this.userService.updateUserInfo();
    }
    this.route.params.subscribe(
      params => {
        if (this.personalHolderService.personalImage != null) {
          this.userImage = this.personalHolderService.personalImage;
          this.fillFields();
        }

        this.imageService
          .getUserImage(params['id'])
          .then(image => {
            this.userImage = image;
            this.fillFields();
          });
      }
    );

  }

  goBack() {
    this.location.back();
  }

  getCreationDate(): string {
    return this.creationDate
  }

  saveDescription() {
    if (this.userImage.user_id != this.userInfo.id) return;
    this.imageService
      .editUserImageDescription(this.userImage.id, this.userImage.description)
      .then(userImage => this.userImage = userImage);
  }

  saveTitle() {
    if (this.userImage.user_id != this.userInfo.id) return;
    this.imageService
      .editUserImageName(this.userImage.id, this.userImage.name)
      .then(userImage => this.userImage = userImage);
  }

  private fillFields() {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }

  copyToClipBoard(object: any) {
    console.info(object);
    object.select();
    document.execCommand("copy");
  }

  autoGrow() {
    let element: HTMLElement = document.getElementById("desc");
    element.style.height = "5px";
    element.style.height = (element.scrollHeight) + "px";
  }
}
