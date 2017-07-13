import {Component} from "@angular/core";
import {ImageService} from "../service/image.service";
import {UserImage} from "../models/user-image.model";
/**
 * Created by ermolaev on 7/11/17.
 */

@Component({
  templateUrl: './../templates/personal-images.template.html',
  styleUrls: ['./../css/personal-images.style.css']
})
export class PersonalImagesComponent {
  images : Array<UserImage>;

  constructor(private imageService : ImageService) {
    imageService.getUserImages().then(images => {
      this.images = images;
    })
  }


}
