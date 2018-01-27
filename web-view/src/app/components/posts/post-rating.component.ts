/**
 * Created by ermolaev on 5/14/17.
 */

import {Component, Input} from '@angular/core';
import {Post} from "../../models/post/post.model";

@Component({
  selector: "s-post-rating",
  templateUrl: '../../templates/posts/post-rating.template.html'
})
export class PostRatingComponent {
  @Input("post") post: Post;
}
