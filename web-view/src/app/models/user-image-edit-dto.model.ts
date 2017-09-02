/**
 * Created by ermolaev on 7/3/17.
 */

export class UserImageEditDto {
  user_image_id : number;
  field : string;


  constructor(user_image_id: number, field: string) {
    this.user_image_id = user_image_id;
    this.field = field;
  }
}
