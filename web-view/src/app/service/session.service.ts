
import {environment} from "../../environments/environment";
import {Injectable} from "@angular/core";

@Injectable()
export class SessionService {

  public getUserSession() {
    return new Promise((resolve, reject) => {
      let sessionID: string = localStorage.getItem(environment.tokenName);
      if (sessionID == null) {
        return reject(null)
      }
      return resolve(sessionID)

    });
  }
}
