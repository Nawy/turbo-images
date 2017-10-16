import {Component, OnInit} from "@angular/core";
import {PersonalHolderService} from "../../service/personal-holder.service";
import {UserImage} from "../../models/user-image.model";
import {ActivatedRoute} from "@angular/router";
import {ImageService} from "../../service/image.service";
import {Location} from '@angular/common';
import {environment} from "../../../environments/environment";
import {UserInfo} from "../../models/user-info.model";
import {UserService} from "app/service/user.service";
import {Post} from "../../models/post.model";
import {PostService} from "../../service/post.service";

/**
 * Created by ermolaev on 7/23/17.
 */

@Component({
  templateUrl: './../../templates/images/image-page.template.html',
  styleUrls: ['./../../css/ImagePageComponent.style.css']
})
export class ImagePageComponent implements OnInit {

  private autoUpdateInterval: number = environment.autoUpdateInterval;
  private updateTimer;
  userInfo: UserInfo = null;

  userImage: UserImage = null;
  imageSource: string;
  creationDate: string;

  constructor(private route: ActivatedRoute,
              private imageService: ImageService,
              private location: Location,
              private personalHolderService: PersonalHolderService,
              private userService: UserService,
              private postService: PostService) {
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
            // this required because autogrow work only if window component is loaded and filled with data
            setTimeout(() => this.autoGrow(), 20)
          });
      }
    );
  }

  deferredTitleUpdate() {
    clearTimeout(this.updateTimer);
    this.updateTimer = setTimeout(
      () => this.saveTitle(),
      this.autoUpdateInterval
    );
  }

  deferredDescriptionUpdate() {
    clearTimeout(this.updateTimer);
    this.updateTimer = setTimeout(
      () => this.saveDescription(),
      this.autoUpdateInterval
    );
  }

  saveDescription() {
    this.imageService
      .editUserImageDescription(this.userImage.id, this.userImage.description)
      .then(userImage => this.userImage = userImage);
  }

  saveTitle() {
    this.imageService
      .editUserImageName(this.userImage.id, this.userImage.name)
      .then(userImage => this.userImage = userImage);
  }

  goBack() {
    this.location.back();
  }

  getCreationDate(): string {
    return this.creationDate
  }

  isReadonly() {
    return this.userImage != null && this.userInfo != null
      && this.userImage.user_id != this.userInfo.id;
  }

  private fillFields() {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
    this.creationDate = this.userImage.creation_date;
  }

  copyToClipBoard(object: any) {
    object.select();
    document.execCommand("copy");
  }

  autoGrow() {
    let element: HTMLElement = document.getElementById("desc");
    element.style.height = "5px";
    element.style.height = (element.scrollHeight) + "px";
  }

  public shareWithCommunity() {
    if (this.isReadonly()) return;
    const newPost: Post = new Post();
    newPost.images = new Map<UserImage, string>([[this.userImage, null]]);
    this.postService.addPost(newPost)
      .then(post => {
        // TODO router route to post
      });
  }
}
