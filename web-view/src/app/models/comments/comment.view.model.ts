import {DeviceType} from "../device-type.model";
import {Rating} from "../rating.model";
import {Comment} from "./comment.model";
import * as moment from 'moment';

export class ViewComment {

  id: string;
  user_id: string;
  user_name: string;
  reply: ViewComment;
  device: DeviceType;
  content: string;
  create_date: Date;
  rating: Rating;

  constructor(comment: Comment, parentComment: Comment) {
    this.id = comment.id;
    this.user_id = comment.user_id;
    this.user_name = comment.user_name;

    this.reply = parentComment? new ViewComment(parentComment, null) : null;
    this.device = comment.device;
    this.content = comment.content;
    this.create_date = moment(comment.create_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate();
    this.rating = comment.rating ? comment.rating : new Rating();
  }

}
