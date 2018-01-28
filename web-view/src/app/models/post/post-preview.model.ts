/**
 * Created by ermolaev on 5/9/17.
 */
import {DeviceType} from "../device-type.model";
import {Rating} from "../rating.model";

export class PostPreview {

  id: string;
  name: string;
  description: string;
  rating: Rating;
  views: number;
  preview_image: string;
  device_type: DeviceType;
  tags: Array<string>;
  create_date: string;

}
