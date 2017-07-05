import {Injectable} from "@angular/core";
import {Headers, Http, Response} from "@angular/http";
import {UserInfo} from "../models/user-info.model";
import {environment} from "../../environments/environment";
/**
 * Created by ermolaev on 7/4/17.
 */


@Injectable()
export class UserService {

  constructor(private http: Http) {
  }

  public getUserInfo() : Promise<UserInfo> {
    return new Promise((resolve, reject) => {
      let sessionID: string = localStorage.getItem(environment.tokenName);
      if(sessionID == null) {
        return reject(null)
      }
      return resolve(sessionID)

    }).then(sessionID => {

      const url = `${environment.host}${environment.requests.getUserInfoUrl}`;
      return this.http.get(
        url,
        {headers: new Headers({"session": sessionID})}
      ).toPromise()
      .then(res => {
        return res.json() as UserInfo
      })
    });
  }
}
