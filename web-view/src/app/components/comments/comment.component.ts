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

  diffDays: number;
  diffHours: number;
  diffMinutes:number;
  diffSeconds: number;


  ngOnInit(): void {
    let momentNow = moment(new Date());
    let momentCreateDate = moment(this.currentComment.create_date);

    this.diffDays = momentNow.dayOfYear() - momentCreateDate.dayOfYear();
    this.diffHours = momentNow.hours() - momentCreateDate.hours();
    this.diffMinutes = momentNow.minutes() - momentCreateDate.minutes();
    this.diffSeconds = momentNow.seconds() - momentNow.seconds();
  }

}
