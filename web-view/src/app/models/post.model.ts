import {UserImage} from "./user-image.model";

export class Post {

  name : string;
  description : string;
  images : Array<UserImage>;
  deviceType : string;
  tags : Array<string>;
}
