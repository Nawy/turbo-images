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
}
