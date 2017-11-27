/**
 * Created by ermolaev on 5/14/17.
 */

import {Component, Input} from '@angular/core';
import {PostPreview} from "../../models/post/post-preview.model";
import {environment} from "../../../environments/environment";
import {Router} from "@angular/router";

@Component({
  selector: "s-postpreview",
  templateUrl: '../../templates/posts/post-preview.template.html'
})
export class PostPreviewComponent {
  @Input("post") post: PostPreview;

  //style
  activeClass: string;
  defaultClass: string;
  useClass: string;

  //data
  hintHeaderString: string;

  constructor(
    private router:Router
  ) {
    this.activeClass = "postpreview-active";
    this.defaultClass = "postpreview-default";

    this.useClass = this.defaultClass;
  }

  getHeaderString(): string {
    return "Rating " + this.post.rating;
  }

  onMouseOver(): void {
    this.useClass = this.activeClass;
  }

  onMouseOut(): void {
    this.useClass = this.defaultClass;
  }

  getThumbnail(): string {
    return `http://${environment.imageHost}${this.post.preview_image}`
  }

  showPost() {
    this.router.navigateByUrl("post/" + this.post.id);
  }
}
