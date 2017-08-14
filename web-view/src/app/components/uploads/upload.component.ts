import {Component} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {UserImage} from "../../models/user-image.model";
import {Subject} from "rxjs/Subject";
/**
 * Created by ermolaev on 7/9/17.
 */


@Component({
  selector: "s-upload",
  templateUrl: './../../templates/uploads/upload.template.html',
  styleUrls: ['./../../css/upload.style.css'],
})
export class UploadComponent {

  images : Array<UserImage>;
  private uploadedImage = new Subject<UserImage>();

  constructor(private imageService : ImageService) {
    this.images = [];
    this.uploadedImage.subscribe(userImage => this.images.push(userImage));

    for (let file of imageService.uploadFiles) {
      this.imageService.uploadImage(file).then(userImage => this.uploadedImage.next(userImage));
    }
  }

}
