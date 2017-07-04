import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {UserInfo} from "../models/user-info.model";
import {environment} from "../../environments/environment";
/**
 * Created by ermolaev on 7/4/17.
 */


@Injectable()
export class UserService {

  constructor(private http: Http) {
  }

  public getUserInfo() : UserInfo {
    const url = `${environment.host}${environment.requests.getUserInfoUrl}`;
    return null;
  }
}
