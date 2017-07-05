/**
 * Created by ermolaev on 6/27/17.
 */

import {Injectable} from "@angular/core";
import {environment} from "environments/environment";
import {Headers, Http, Response} from "@angular/http";
import {UserSignup} from "../models/user-signup.model";
import {jsonHeader} from "../utils/http.utils";
import 'rxjs/add/operator/toPromise';
import {UserSignin} from "../models/user-signin.model";

class IsExistsClass {
  exists : boolean;
}

@Injectable()
export class AuthorizationService {

  constructor(private http: Http) {
  }

  public signup(signupData: UserSignup) : Promise<string> {
    const url = `${environment.host}${environment.requests.signupUrl}`;
    return this.http
      .post(url, JSON.stringify(signupData), {headers: jsonHeader})
      .toPromise()
      .then(res => null)
      .catch(this.errorHandler)
  }

  public signin(signinData: UserSignin) : Promise<string> {
    const url = `${environment.host}${environment.requests.signinUrl}`;
    return this.http
      .post(url, JSON.stringify(signinData), {headers: jsonHeader})
      .toPromise()
      .then(res => {
        let sessionId = res.headers.get("session");
        localStorage.setItem(environment.tokenName, sessionId);
        return sessionId;
      })
      .catch(this.errorHandler)
  }

  public logout() : Promise<boolean> {
    let sessionID: string = localStorage.getItem(environment.tokenName);
    const url = `${environment.host}${environment.requests.logoutUrl}`;
    return this.http
      .post(
        url,
        null,
        {headers: new Headers({"session": sessionID})}
      )
      .toPromise()
      .then(res => {
        localStorage.removeItem(environment.tokenName)
        return true;
      })
      .catch(this.errorHandler)
  }

  public isExistsNameOrEmail(name : string) : Promise<boolean> {
    const url = `${environment.host}${environment.requests.isUserExistsUrl}`;
    return this.http
      .get(url, {
        headers: jsonHeader,
        params: {
          "name_or_email": name
        }
      })
      .toPromise()
      .then(res => {
        let response : IsExistsClass = res.json() as IsExistsClass;
        return response.exists;
      })
      .catch(res => {
        return false
      })
  }

  successHandler(response : Response) : string {
    return null;
  }

  errorHandler(response : Response) {
    return Promise.reject(response.json().message);
  }
}
