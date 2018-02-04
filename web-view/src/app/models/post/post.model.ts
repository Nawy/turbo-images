import {DeviceType} from "../device-type.model";
import {Rating} from "../rating.model";
import {UserImage} from "../user-image.model";
import {Comment} from "../comments/comment.model";

export class Post {

  id: string;
  name: string;
  description: string;
  visible:boolean;
  create_date: string;

  rating: Rating;
  views: number;

  images : Array<UserImage>;
  device_type: DeviceType;
  tags: Array<string>;

  user_id: string;
  user_name: string;

  comments // object of Comments

}
