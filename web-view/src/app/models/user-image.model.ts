import {environment} from "../../environments/environment";

/**
 * Created by ermolaev on 7/12/17.
 */

export class Image {
  id: string;
  source: string;
  thumbnail: string;
}

export class UserImage {
  id: string;
  image: Image;
  name: string;
  description: string;
  creation_date: string;
  user_id: string;
}
