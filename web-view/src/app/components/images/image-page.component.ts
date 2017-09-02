import {Component, OnInit} from "@angular/core";
import {PersonalHolderService} from "../../service/personal-holder.service";
import {UserImage} from "../../models/user-image.model";
import {ActivatedRoute} from "@angular/router";
import {ImageService} from "../../service/image.service";
import {Location} from '@angular/common';
import {environment} from "../../../environments/environment";
import {UserImageEditFieldForm} from "../../models/forms/user-image-edit-form.model";

/**
 * Created by ermolaev on 7/23/17.
 */

@Component({
  templateUrl: './../../templates/images/image-page.template.html',
  styleUrls: ['./../../css/ImagePageComponent.style.css']
})
export class ImagePageComponent implements OnInit {

  private DEFAULT_NAME_TITLE: string = "Give your image a title ...";
  private DEFAULT_DESCRIPTION_TITLE: string = "Image description";

  userImage: UserImage;
  imageSource: string;
  creationDate: string;

  changeName: UserImageEditFieldForm = {
    field: ""
  };

  changeDescription: UserImageEditFieldForm = {
    field: ""
  };

  constructor(private route: ActivatedRoute,
              private imageService: ImageService,
              private location: Location,
              private personalHolderService: PersonalHolderService) {
  }

  ngOnInit(): void {
    window.scrollTo(0, 0);

    if (this.userImage == null) {
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
      )
    }
  }

  goBack() {
    this.location.back();
  }

  getCreationDate(): string {
    return this.creationDate
  }

  save() {
    const name: string = this.changeName.field;
    if (name != this.DEFAULT_NAME_TITLE && name != this.userImage.name) {
      this.imageService.editUserImageName(this.userImage.id, name)
        .then(userImage => this.userImage = userImage);
    }
    const description: string = this.changeDescription.field;
    if (description != this.DEFAULT_DESCRIPTION_TITLE && description != this.userImage.description) {
      this.imageService.editUserImageDescription(this.userImage.id, description)
        .then(userImage => this.userImage = userImage);
    }
  }

  private fillFields() {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
    this.changeName.field = this.userImage.name == null || this.userImage.name == "" ? this.DEFAULT_NAME_TITLE : this.userImage.name;
    this.changeDescription.field = this.userImage.description == null || this.userImage.description == "" ? this.DEFAULT_DESCRIPTION_TITLE : this.userImage.description;
  }

  copyToClipBoard(object: any) {
    console.info(object);
    object.select();
    document.execCommand("copy");
  }
}
