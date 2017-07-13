/**
 * Created by ermolaev on 5/14/17.
 */

import {Component, Input} from '@angular/core';
import {PostPreview} from "../models/postpreview.model";
import {UserImage} from "../models/user-image.model";
import {environment} from "../../environments/environment";

@Component({
  selector: "s-image-preview",
  templateUrl: './../templates/image-preview.template.html'
})
export class ImagePreviewComponent {
  @Input("user_image") userImage: UserImage;

  //style
  activeClass : string;
  defaultClass : string;
  useClass : string;

  //data
  hintHeaderString: string;

  constructor() {
    this.activeClass = "postpreview-active";
    this.defaultClass = "postpreview-default";

    this.useClass = this.defaultClass;
  }

  getThumbnail() : string {
    return `http://${environment.imageHost}${this.userImage.image.thumbnail}`
  }

  getHeaderString() : string {
    return "Rating " + this.userImage.create_date;
  }

  onMouseOver() : void {
    this.useClass = this.activeClass;
  }

  onMouseOut() : void {
    this.useClass = this.defaultClass;
  }
}
