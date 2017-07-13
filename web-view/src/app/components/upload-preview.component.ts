/**
 * Created by ermolaev on 7/13/17.
 */
import {Component, Input} from "@angular/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {UserImage} from "../models/user-image.model";
import {environment} from "../../environments/environment";
/**
 * Created by ermolaev on 7/9/17.
 */

@Component({
  selector: "s-upload-preview",
  templateUrl: './../templates/upload-preview.template.html',
  styleUrls: ['./../css/upload-preview.style.css'],
})
export class UploadPreviewComponent {
  @Input("user_image") userImage : UserImage;

  constructor() {}

  getSource() : string {
    console.info(this.userImage);
    let imagePath = `http://${environment.imageHost}${this.userImage.image.source}`;
    console.info(imagePath);
    return imagePath;
  }
}
