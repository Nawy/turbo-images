import {Headers, Http} from "@angular/http";
import {Injectable} from "@angular/core";
import {environment} from "environments/environment";
/**
 * Created by ermolaev on 7/10/17.
 */

@Injectable()
export class ImageService {

  constructor(private http: Http) {
  }

  uploadImage(file : File) {
    const url = `${environment.host}${environment.requests.addUserImageUrl}`;
    let sessionID: string = localStorage.getItem(environment.tokenName);


    let formData : FormData = new FormData();
    formData.append("file", file);
    formData.append("name", "test.jpg");
    this.http.post(
      url,
      formData,
      {headers: new Headers({"session": sessionID})}
    ).toPromise().catch(res => console.error(res))
  }
}
