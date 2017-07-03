/**
 * Created by ermolaev on 7/3/17.
 */

export class UserSignin {
  name : string;
  password : string;

  constructor(name: string = "", password: string = "") {
    this.name = name;
    this.password = password;
  }
}
