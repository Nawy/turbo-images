/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core';
import {Comment} from './../../models/comments/comment.model'

@Component({
  selector: "s-comments-list",
  templateUrl: './../../templates/comments/comments-list.template.html'
})
export class CommentsListComponent {
  commentsList : Array<Comment>;

  constructor() {
    this.commentsList = [
      new Comment("Comments it's comment !!! Whooo! sdfasdfdsafdsf dfasfsdafasdfasdf adsfasdfsdfad adsf ddfasdf", 11, 234, null),
      new Comment("Another comment yeah!", 4, 3, [
        new Comment("Another comment yeah!", 4, 3, null),
        new Comment("And again, cool!", 23424, 456, null)
      ]),
      new Comment("And again, cool!", 23424, 456, null)
    ];
  }
}
