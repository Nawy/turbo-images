import {DeviceType} from "../device-type.model";
import {Rating} from "../rating.model";

export class Comment {

  id: string;
  user_id: string;
  user_name: string;
  reply_id: string;
  device: DeviceType = DeviceType.UNKNOWN;
  content: string;
  create_date: string;
  rating: Rating;
  deleted: boolean;

}
