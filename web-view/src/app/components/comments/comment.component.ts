/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core';
import {ViewComment} from "../../models/comments/comment.view.model";
import * as moment from 'moment';
import {UserInfo} from "../../models/user-info.model";
import {UserService} from "../../service/user.service";

@Component({
  selector: "s-comment",
  templateUrl: '../../templates/comments/comment.template.html',
  styleUrls: ['../../css/comment.style.css']
})
export class CommentComponent {
  @Input("comment") currentComment: ViewComment;
  @Input("canEdit") canEdit: boolean = false;
  userInfo:UserInfo;

  @Input("reply_function") replyFunction: Function;
  @Input("delete_function") deleteFunction: Function;

  diffDays: number;
  diffHours: number;
  diffMinutes:number;
  diffSeconds: number;

  startReplyCreating: boolean = false;
  replyCommentContent: string = "";

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    let momentNow = moment(new Date());
    let momentCreateDate = moment(this.currentComment.create_date);

    this.diffDays = momentNow.dayOfYear() - momentCreateDate.dayOfYear();
    this.diffHours = momentNow.hours() - momentCreateDate.hours();
    this.diffMinutes = momentNow.minutes() - momentCreateDate.minutes();
    this.diffSeconds = momentNow.seconds() - momentNow.seconds();

    this.userService.userInfoSource.subscribe(userInfo => {
      this.userInfo = userInfo;
      // this required because autogrow work only if window component is loaded and filled with data
      setTimeout(() => this.autoGrow(), 20);
    });
    if (this.userInfo == null) this.userService.updateUserInfo();
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

  canEditFunction():boolean{
    if (!this.canEdit) return false;
    if (!this.userInfo) return false;
    return this.currentComment.user_id === this.userInfo.id;
  }

}
