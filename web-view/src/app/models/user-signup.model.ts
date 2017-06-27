/**
 * Created by ermolaev on 6/27/17.
 */

export class UserSignup {

  name : string;
  password : string;
  email : string;

  constructor(name: string = "", password: string = "", email: string = "") {
    this.name = name;
    this.password = password;
    this.email = email;
  }
}
