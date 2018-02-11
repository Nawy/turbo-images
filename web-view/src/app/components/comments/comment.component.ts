/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core';
import {ViewComment} from "../../models/comments/comment.view.model";
import * as moment from 'moment';

@Component({
  selector: "s-comment",
  templateUrl: '../../templates/comments/comment.template.html',
  styleUrls: ['../../css/comment.style.css']
})
export class CommentComponent {
  @Input("comment") currentComment: ViewComment;
  @Input("reply") reply: boolean = false;

  @Input("reply_function") replyFunction: Function;
  @Input("delete_function") deleteFunction: Function;

  diffDays: number;
  diffHours: number;
  diffMinutes:number;
  diffSeconds: number;

  startReplyCreating: boolean = false;
  replyCommentContent: string = "";


  ngOnInit(): void {
    let momentNow = moment(new Date());
    let momentCreateDate = moment(this.currentComment.create_date);

    this.diffDays = momentNow.dayOfYear() - momentCreateDate.dayOfYear();
    this.diffHours = momentNow.hours() - momentCreateDate.hours();
    this.diffMinutes = momentNow.minutes() - momentCreateDate.minutes();
    this.diffSeconds = momentNow.seconds() - momentNow.seconds();
  }

  replyComment() {
    this.replyFunction(this.replyCommentContent, this.currentComment.id);
    this.replyCommentContent = "";
  }

  deleteComment() {
    this.deleteFunction(this.currentComment.id);
  }

  autoGrow() {
    let elements: any = document.getElementsByClassName("post-description");

    for (let element of elements) {
      element.style.height = "5px";
      element.style.height = (element.scrollHeight) + "px";
    }
  }

  changeReplyStatus(){
    this.startReplyCreating = !this.startReplyCreating;
  }

}
