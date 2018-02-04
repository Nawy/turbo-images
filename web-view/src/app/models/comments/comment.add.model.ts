import {DeviceType} from "../device-type.model";

export class AddCommentDto {

  post_id: string;
  reply_id: string;
  device: DeviceType = DeviceType.UNKNOWN;
  content: string;
}
