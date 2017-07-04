/**
 * Created by ermolaev on 7/3/17.
 */

export class UserSignin {
  email_or_name : string;
  password : string;

  constructor(email_or_name: string = "", password: string = "") {
    this.email_or_name = email_or_name;
    this.password = password;
  }
}
