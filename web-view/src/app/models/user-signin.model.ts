/**
 * Created by ermolaev on 7/3/17.
 */

export class UserSignin {
  email : string;
  password : string;

  constructor(email: string = "", password: string = "") {
    this.email = email;
    this.password = password;
  }
}
