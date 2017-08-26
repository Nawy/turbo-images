import {Component, OnInit} from "@angular/core";
import {PersonalHolderService} from "../../service/personal-holder.service";
import {UserImage} from "../../models/user-image.model";
import {ActivatedRoute} from "@angular/router";
import {ImageService} from "../../service/image.service";
import {Location} from '@angular/common';
import {environment} from "../../../environments/environment";

/**
 * Created by ermolaev on 7/23/17.
 */

@Component({
  templateUrl: './../../templates/images/image-page.template.html',
})
export class ImagePageComponent implements OnInit {
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

  private createImageSource() {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }
}
