/**
 * Created by ermolaev on 6/27/17.
 */

import {Injectable} from "@angular/core";
import {environment} from "environments/environment";
import {Http, Response} from "@angular/http";
import {UserSignup} from "../models/user-signup.model";
import {jsonHeader} from "../utils/http.utils";
import 'rxjs/add/operator/toPromise';
import {UserSignin} from "../models/user-signin.model";

@Injectable()
export class AuthorizationService {

  constructor(private http: Http) {
  }

  public signup(signupData: UserSignup) : Promise<string> {
    const url = `${environment.host}${environment.requests.signupUrl}`;
    return this.http
      .post(url, JSON.stringify(signupData), {headers: jsonHeader})
      .toPromise()
      .then(this.successHandler)
      .catch(this.errorHandler)
  }

  public signin(signinData: UserSignin) {
    const url = `${environment.host}${environment.requests.signinUrl}`;
    return this.http
      .post(url, JSON.stringify(signinData), {headers: jsonHeader})
      .toPromise()
      .then(this.successHandler)
      .catch(this.errorHandler)
  }

  // public checkName(name : string) : Promise<boolean> {
  //
  // }

  successHandler(response : Response) : string {
    return null;
  }

  errorHandler(response : Response) : string {
    return response.json().message;
  }
}
