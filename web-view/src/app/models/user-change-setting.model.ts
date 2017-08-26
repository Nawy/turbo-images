/**
 * Created by ermolaev on 7/3/17.
 */

export class UserChangePassword {
  old_password: string;
  new_password: string;

  constructor(oldPassword: string, newPassword: string) {
    this.old_password = oldPassword;
    this.new_password = newPassword;
  }
}

export class UserChangeField {
  new_user_field: string;

  constructor(new_user_field: string) {
    this.new_user_field = new_user_field;
  }
}
