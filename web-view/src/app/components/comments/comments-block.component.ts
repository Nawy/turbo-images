/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, Input} from '@angular/core';
import {ViewComment} from "../../models/comments/comment.view.model";
import {UserInfo} from "../../models/user-info.model";
import {UserService} from "../../service/user.service";
import {AddCommentDto} from "app/models/comments/comment.add.model";
import {CommentService} from "../../service/comment.service";


@Component({
  selector: "comments-block",
  templateUrl: '../../templates/comments/comments-block.template.html'
})
export class CommentsBlockComponent {
  @Input("postId") postId: string;
  @Input("comments") comments = {}; //object of objects
  userInfo: UserInfo;

  newComment: string = "";

  sortedByDateCommentList: Array<ViewComment> = [];
  timeSort: boolean = true;

  boundDeleteFunction: Function;
  boundReplyFunction: Function;

  constructor(private userService: UserService, private commentService: CommentService) {
  }

  ngOnInit(): void {
    this.boundDeleteFunction = this.deleteComment.bind(this);
    this.boundReplyFunction = this.replyComment.bind(this);

    this.userService.userInfoSource.subscribe(userInfo => {
      this.userInfo = userInfo;
      // this required because autogrow work only if window component is loaded and filled with data
      setTimeout(() => this.autoGrow(), 20);
    });
    if (this.userInfo == null) this.userService.updateUserInfo();
    this.resortComments();
  }

  setTimeSort() {
    this.timeSort = true;
    this.resortComments();
  }

  resetTimeSort() {
    this.timeSort = false;
    this.resortComments();
  }

  resortComments() {
    let sorting = this.timeSort ?
      (comment1, comment2) => {
        let comment1Date = comment1.create_date;
        let comment2Date = comment2.create_date;
        return comment1Date > comment2Date ? -1 : comment1Date < comment2Date ? 1 : 0;
      } :
      (comment1, comment2) => {
        let comment1Rating = comment1.rating.rating;
        let comment2Rating = comment2.rating.rating;
        return comment1Rating > comment2Rating ? -1 : comment1Rating < comment2Rating ? 1 : 0;
      };

    this.sortedByDateCommentList = Object.keys(this.comments)
      .map(key => this.comments[key])
      .map(comment =>
        new ViewComment(
          comment,
          this.comments[comment.reply_id]
        )
      )
      .filter(viewComment => !viewComment.deleted)
      .sort(sorting);
  }

  autoGrow() {
    let elements: any = document.getElementsByClassName("post-description");

    for (let element of elements) {
      element.style.height = "5px";
      element.style.height = (element.scrollHeight) + "px";
    }
  }

  addComment() {
    let addCommentDto: AddCommentDto = new AddCommentDto();
    addCommentDto.content = this.newComment;
    addCommentDto.post_id = this.postId;
    this.commentService.addComment(addCommentDto)
      .then(() => this.refreshComments());
  }

  replyComment(content: string, replyCommentId: string) {
    let addCommentDto: AddCommentDto = new AddCommentDto();
    addCommentDto.content = content;
    addCommentDto.post_id = this.postId;
    addCommentDto.reply_id = replyCommentId;
    this.commentService.addComment(addCommentDto)
      .then(() => this.refreshComments());
  }

  deleteComment(commentId: string) {
    this.commentService.deleteComment(this.postId, commentId)
      .then(() => this.refreshComments());
  }

  changeCommentRating(commentId: string, rating: boolean) {
    this.commentService.changeCommentRating(this.postId, commentId, rating)
      .then(() => this.refreshComments());
  }

  refreshComments() {
    this.commentService.getComments(this.postId).then(comment => {
      this.comments = comment;
      this.resortComments();
      this.newComment = "";
    });
  }

}
