import {DeviceType} from "../device-type.model";
import {UserImage} from "../user-image.model";

export class Post {

  id: string;
  name: string;
  description: string;
  visible:boolean;
  create_date: string;

  ups: number;
  downs: number;
  rating: number;
  views: number;

  images : Array<UserImage>;
  device_type: DeviceType;
  tags: Array<string>;

  user_id: string;
  user_name: string;

}
