
import {PostPreview} from "./post/post-preview.model";
import * as moment from 'moment';

export class UserPostsMap {
  creationDate: Date;
  posts: Array<PostPreview>;


  constructor(creationDate: Date, posts: Array<PostPreview>) {
    this.creationDate = creationDate;
    this.posts = posts;
  }

  getCreationDate(): string {
    let currentDate = new Date().getDate();
    if (this.creationDate.getDate() == currentDate) {
      return "Today";
    }
    if (moment(this.creationDate).add(1, 'd').toDate().getDate() == currentDate) {
      return "Yesterday";
    }
    return moment(this.creationDate).format("D MMMM");
  }
}
