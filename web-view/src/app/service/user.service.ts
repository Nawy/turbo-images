import {Injectable} from "@angular/core";
import {Headers, Http, Response} from "@angular/http";
import {UserInfo} from "../models/user-info.model";
import {environment} from "../../environments/environment";
import {Subject} from "rxjs/Subject";
/**
 * Created by ermolaev on 7/4/17.
 */


@Injectable()
export class UserService {

  private userInfo : UserInfo;
  private userInfoSource = new Subject<UserInfo>();
  userInfoObserver$ = this.userInfoSource.asObservable();

  constructor(private http: Http) {
    this.userInfoObserver$.subscribe(userInfo => this.userInfo = userInfo);
  }

  public updateUserInfo() : Promise<UserInfo> {
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
        let userInfo = res.json() as UserInfo;
        this.userInfoSource.next(userInfo);
        return userInfo
      })
    }).catch(res => {
      if(res.code() != 500) {
        localStorage.removeItem(environment.tokenName);
      }
      this.userInfoSource.next(null);
    });
  }

  public isLoggedIn() : boolean {
    let token = localStorage.getItem(environment.tokenName);
    return token != null;
  }
}
