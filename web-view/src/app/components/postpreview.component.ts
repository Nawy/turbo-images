/**
 * Created by ermolaev on 5/14/17.
 */

import {Component, Input} from '@angular/core';
import {PostPreview} from "../models/postpreview.model";

@Component({
  selector: "s-postpreview",
  templateUrl: './../templates/postpreview.template.html'
})
export class PostPreviewComponent {
  @Input("postdata") postdata: PostPreview;

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

  getHeaderString() : string {
    return "Rating " + this.postdata.rating;
  }

  onMouseOver() : void {
    this.useClass = this.activeClass;
  }

  onMouseOut() : void {
    this.useClass = this.defaultClass;
  }
}
