import {UserImage} from "./user-image.model";

export class Post {

  name : string;
  description : string;
  images : Map<UserImage,string>;
  deviceType : string;
  tags : Array<string>;
}
