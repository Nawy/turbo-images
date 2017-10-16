import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {Post} from "../models/post.model";
import {environment} from "../../environments/environment";
import {SessionService} from "./session.service";

class PostDto {
  name: string;
  description: string;
  image_ids: Array<string>;
  device_type: string;
  tags: Array<string>;
  visible: boolean;

  constructor(that: Post) {
    this.name = that.name;
    this.description = that.description;
    this.image_ids = that.images.map(image => image.id);
    this.device_type = environment.clientType;
    this.tags = that.tags;
    this.visible = true;
  }
}

/**
 * Created by ermolaev on 10/11/17.
 */
@Injectable()
export class PostService {

  constructor(private http: Http, private sessionService: SessionService) {
  }

  /**
   * Add new post
   * @param {Post} post
   * @returns {Promise<Post>}
   */
  public addPost(post: Post): Promise<Post> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.addPost}`;
        console.info(url);
        return this.http
          .post(url, new PostDto(post), {headers: new Headers({"session": sessionID})})
          .toPromise()
          .then(value => {
            console.info(value);
            return value;
          })
      });
  }

}
