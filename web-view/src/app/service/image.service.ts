import {Headers, Http} from "@angular/http";
import {Injectable} from "@angular/core";
import {environment} from "environments/environment";
import {UserImage} from "../models/user-image.model";
/**
 * Created by ermolaev on 7/10/17.
 */

@Injectable()
export class ImageService {

  uploadFiles : Array<File>;

  constructor(private http: Http) {
  }

  getUserImages() : Promise<Array<UserImage>> {
    return new Promise((resolve, reject) => {
      let sessionID: string = localStorage.getItem(environment.tokenName);
      if(sessionID == null) {
        return reject(null)
      }
      return resolve(sessionID)

    }).then(sessionID => {
      const url = `${environment.host}${environment.requests.getUserImageUrl}`;
      return this.http.get(
        url,
        {headers: new Headers({"session": sessionID})}
      ).toPromise()
        .then(res => {
          let images = res.json() as Array<UserImage>;
          return images
        })
    }).catch(res => {
    });
  }

  uploadImage(file : File) : Promise<UserImage> {
    return new Promise((resolve, reject) => {
      let sessionID: string = localStorage.getItem(environment.tokenName);
      if(sessionID == null) {
        return reject(null)
      }
      return resolve(sessionID)
    }).then(sessionID => {
      const url = `${environment.host}${environment.requests.addUserImageUrl}`;

      let formData : FormData = new FormData();
      formData.append("file", file);
      return this.http.post(
        url,
        formData,
        {headers: new Headers({"session": sessionID})}
      ).toPromise()
      .then(res => {
        let result = res.json() as UserImage;
        return Promise.resolve(result);
      })
    })
  }
}
