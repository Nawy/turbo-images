import {UserImage} from "../user-image.model";

export class addPostDto {

  name : string;
  description : string;
  images : Array<UserImage>;
  deviceType : string;
  tags : Array<string>;

}
