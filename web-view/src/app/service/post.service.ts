import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {TransferPost} from "../models/post/add-post-dto.model";
import {environment} from "../../environments/environment";
import {SessionService} from "./session.service";
import {PostPreview} from "../models/post/post-preview.model";
import * as moment from 'moment';
import {Post} from "../models/post/post.model";
import {PostRatingModel} from "../models/post/post-rating.model";


/**
 * Created by ermolaev on 10/11/17.
 */
@Injectable()
export class PostService {

  constructor(private http: Http, private sessionService: SessionService) {
  }

  /**
   * Add new post
   * @param {TransferPost} post
   * @returns {Promise<TransferPost>}
   */
  public savePost(post: TransferPost): Promise<Post> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.savePost}`;
        console.info(url);
        return this.http.post(url, post, {headers: new Headers({"session": sessionID})})
          .toPromise()
          .then(value => value.json() as Post)
      });
  }

  public getUserPosts(startDate: Date): Promise<Array<PostPreview>> {
    return this.sessionService.getUserSession()
      .then(sessionID =>
        this.http.get(
          `${environment.host}${environment.requests.getUserPostsByDate}`,
          {
            headers: new Headers({"session": sessionID}),
            params: {"date": moment(startDate).format("YYYY-MM-DD HH:mm:ss.SSS")}
          }
        )
          .toPromise()
          .then(res => res.json() as Array<PostPreview>)
      ).catch(res => Promise.reject(res));
  }

  public getPosts(startDate: Date): Promise<Array<PostPreview>> {
    return this.http.get(
      `${environment.host}${environment.requests.getPostsByDate}`,
      {
        params: {"date": moment(startDate).format("YYYY-MM-DD HH:mm:ss.SSS")}
      }
    ).toPromise()
      .then(res => res.json() as Array<PostPreview>)
      .catch(res => Promise.reject(res));
  }

  public getPost(id: string): Promise<Post> {
    return this.http.get(`${environment.host}${environment.requests.getPost}${id}`)
      .toPromise()
      .then(res => {
        let result = res.json() as Post;

        this.increaseViews(result.id)
          .catch(
            res => console.error("Cannot update views!")
          );

        return result;
      })
      .catch(res => Promise.reject(res));
  }

  public increaseViews(id: string): Promise<Boolean> {
    return this.http.post(
      `${environment.host}${environment.requests.increaseViews}`,
      new PostRatingModel(id, false, false, true),
      null
    ).toPromise()
      .then(res => true)
      .catch(res => false);
  }

  public upvote(id: string): Promise<Boolean> {
    return this.sessionService.getUserSession()
      .then(sessionID =>
        this.http.post(
          `${environment.host}${environment.requests.changeRating}`,
          new PostRatingModel(id, true, false, false),
          {
            headers: new Headers({"session": sessionID})
          }
        ).toPromise()
          .then(res => true)
          .catch(res => false)
      ).catch(res => Promise.reject(res));
  }

  public downvote(id: string): Promise<Boolean> {
    return this.sessionService.getUserSession()
      .then(sessionID =>
        this.http.post(
          `${environment.host}${environment.requests.changeRating}`,
          new PostRatingModel(id, false, true, false),
          {
            headers: new Headers({"session": sessionID})
          }
        ).toPromise()
          .then(res => true)
          .catch(res => false)
      ).catch(res => Promise.reject(res));
  }

  public findPost(value: string): Promise<Array<PostPreview>> {
    return this.http.get(
      `${environment.host}${environment.requests.findPost}`,
      {
        params: {
          query: value
        }
      }
    ).toPromise()
      .then(res => res.json() as Array<PostPreview>)
      .catch(res => Promise.reject(res));
  }
}
