/**
 * Created by ermolaev on 5/9/17.
 */
import {DeviceType} from "./device-type.model";

export class PostPreview {

  id: string;
  name: string;
  description: string;
  ups: number;
  downs: number;
  rating: number;
  views: number;
  preview_image: string;
  deviceType: DeviceType;
  tags: Array<string>;
  create_date: string;

}
