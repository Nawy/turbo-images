import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {UserInfo} from "../models/user-info.model";
import {environment} from "../../environments/environment";
import {Subject} from "rxjs/Subject";
import {UserChangeField, UserChangePassword} from "../models/user-change-setting.model";
import {SessionService} from "./session.service";

/**
 * Created by ermolaev on 7/4/17.
 */


@Injectable()
export class UserService {

  private _userInfoSource = new Subject<UserInfo>();

  constructor(private http: Http, private sessionService: SessionService) {
  }

  public updateUserInfo(): Promise<UserInfo> {
    return new Promise((resolve, reject) => {
      let sessionID: string = localStorage.getItem(environment.tokenName);
      if (sessionID == null) {
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
          this._userInfoSource.next(userInfo);
          return userInfo
        })
    }).catch(res => {
      this.clearUserSession();
    });
  }

  public changePassword(userChangePassword: UserChangePassword): Promise<UserInfo> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
      let url = `${environment.host}${environment.requests.updateUserPassword}`;
      return this.http.post(
        url,
        userChangePassword,
        {headers: new Headers({"session": sessionID})}
      ).toPromise()
        .then(res => {
          let userInfo = res.json() as UserInfo;
          this._userInfoSource.next(userInfo);
          return userInfo
        })
    }).catch(res => {
      return Promise.reject(res.status)
    });
  }

  public changeName(userChangeName: UserChangeField): Promise<UserInfo> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
      let url = `${environment.host}${environment.requests.updateUserName}`;
      return this.http.post(
        url,
        userChangeName,
        {headers: new Headers({"session": sessionID})}
      ).toPromise()
        .then(res => {
          let userInfo = res.json() as UserInfo;
          this._userInfoSource.next(userInfo);
          return userInfo
        })
    }).catch(res => {
      return Promise.reject(res.status)
    });
  }

  public changeEmail(userChangeEmail: UserChangeField): Promise<UserInfo> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
      let url = `${environment.host}${environment.requests.updateUserEmail}`;
      return this.http.post(
        url,
        userChangeEmail,
        {headers: new Headers({"session": sessionID})}
      ).toPromise()
        .then(res => {
          let userInfo = res.json() as UserInfo;
          this._userInfoSource.next(userInfo);
          return userInfo
        })
    }).catch(res => {
      return Promise.reject(res.status)
    });
  }

  get userInfoSource(): Subject<UserInfo> {
    return this._userInfoSource;
  }

  public clearUserSession() {
    localStorage.removeItem(environment.tokenName);
    this._userInfoSource.next(null);
  }

  public isLoggedIn(): boolean {
    let token = localStorage.getItem(environment.tokenName);
    return token != null;
  }
}
