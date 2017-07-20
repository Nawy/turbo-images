/**
 * Created by ermolaev on 7/13/17.
 */
import {Component, Input, OnInit} from "@angular/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {UserImage} from "../../models/user-image.model";
import {environment} from "../../../environments/environment";
/**
 * Created by ermolaev on 7/9/17.
 */

@Component({
  selector: "s-upload-preview",
  templateUrl: './../../templates/uploads/upload-preview.template.html',
  styleUrls: ['./../../css/upload-preview.style.css'],
})
export class UploadPreviewComponent implements OnInit{
  @Input("user_image") userImage : UserImage;
  private imageSource : string;

  constructor() {}

  ngOnInit(): void {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }
}
