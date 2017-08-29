import {UserImage} from "../../models/user-image.model";
import {Component, OnInit} from "@angular/core";
import {Location} from '@angular/common';
import {PersonalHolderService} from "../../service/personal-holder.service";
import {ImageService} from "../../service/image.service";
import {ActivatedRoute} from "@angular/router";
import * as moment from 'moment';
import {environment} from "../../../environments/environment";

@Component({
  templateUrl: './../../templates/images/image-edit-page.template.html',
})
export class ImageEditPageComponent implements OnInit {
  userImage: UserImage;
  imageSource: string;

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
            console.info(this.personalHolderService.personalImage);
            this.userImage = this.personalHolderService.personalImage;
            this.createImageSource();
          }

          this.imageService
            .getUserImage(params['id'])
            .then(image => {
              this.userImage = image;
              this.createImageSource();
            });
        }
      )
    }
  }

  goBack() {
    this.location.back();
  }

  getCreationDate() : string {
    return moment(this.userImage.creation_date, "YYYY-MM-DD HH:mm:ss.SSS").format("[Published] D MMMM [at] HH:mm")
  }

  private createImageSource() {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }
}
