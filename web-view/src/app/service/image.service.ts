import {Headers, Http} from "@angular/http";
import {Injectable} from "@angular/core";
import {environment} from "environments/environment";
import {UserImage} from "../models/user-image.model";
import * as moment from 'moment';
import {Subject} from "rxjs/Subject";
import {SessionService} from "./session.service";
import {UserImageEditDto} from "../models/user-image-edit-dto.model";

/**
 * Created by ermolaev on 7/10/17.
 */

@Injectable()
export class ImageService {

  uploadFiles: Array<File>;
  eventNewFilesIsReady = new Subject<boolean>();

  constructor(private http: Http, private sessionService: SessionService) {
  }

  getUserImage(imageId: string): Promise<UserImage> {
    const url = `${environment.host}${environment.requests.getUserImageByIdUrl}${imageId}`;
    return this.http.get(
      url
    ).toPromise()
      .then(res => {
        console.info(res.json());
        let images = res.json() as UserImage;
        return images
      }).catch(res => {
        return Promise.reject(res);
      });
  }

  getUserImages(startDate: Date): Promise<Array<UserImage>> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.getUserImageUrl}`;
        return this.http.get(
          url,
          {
            headers: new Headers({"session": sessionID}),
            params: {"date": moment(startDate).format("YYYY-MM-DD HH:mm:ss.SSS")}
          }
        ).toPromise()
          .then(res => {
            return res.json() as Array<UserImage>
          })
      }).catch(res => {
        return Promise.reject(res);
      });
  }

  editUserImageName(userImageId: string, name: string): Promise<UserImage> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.editUserImageName}`;
        return this.http.post(
          url,
          new UserImageEditDto(userImageId, name),
          {headers: new Headers({"session": sessionID})}
        ).toPromise()
          .then(res => {
            return res.json() as UserImage
          })
      }).catch(res => {
        return Promise.reject(res);
      });
  }

  editUserImageDescription(userImageId: string, description: string): Promise<UserImage> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.editUserImageDescription}`;
        return this.http.post(
          url,
          new UserImageEditDto(userImageId, description),
          {headers: new Headers({"session": sessionID})}
        ).toPromise()
          .then(res => {
            return res.json() as UserImage
          })
      }).catch(res => {
        return Promise.reject(res);
      });
  }

  uploadImage(file: File): Promise<UserImage> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.addUserImageUrl}`;

        let formData: FormData = new FormData();
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

  // updateUserImage() : Promise<boolean> {
  //
  // }
}
