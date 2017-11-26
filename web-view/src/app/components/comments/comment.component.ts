/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core';

@Component({
  selector: "s-comment",
  templateUrl: '../../templates/comments/comment.template.html',
  styleUrls: ['../../css/comment.style.css']
})
export class CommentComponent {
  @Input("value") comment : Comment;
}
