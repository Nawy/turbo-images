import {Component} from "@angular/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ImageService} from "../service/image.service";
import {UserImage} from "../models/user-image.model";
/**
 * Created by ermolaev on 7/9/17.
 */


@Component({
  selector: "s-upload",
  templateUrl: './../templates/upload.template.html',
  styleUrls: ['./../css/upload.style.css'],
})
export class UploadComponent {

  images : Array<UserImage>;

  constructor(private imageService : ImageService) {
    this.images = [];
    for (var file of imageService.uploadFiles) {
      this.imageService.uploadImage(file).then(userImage => this.images.push(userImage));
    }
  }

}
