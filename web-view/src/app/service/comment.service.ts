import {Injectable} from "@angular/core";
import {AddCommentDto} from "../models/comments/comment.add.model";
import {Headers, Http} from "@angular/http";
import {SessionService} from "./session.service";
import {environment} from "../../environments/environment";

@Injectable()
export class CommentService {

  constructor(private http: Http, private sessionService: SessionService) {
  }

  getComments(postId: string): Promise<Object> {
    const url = `${environment.host}${environment.requests.getComments}/${postId}`;
    return this.http.get(url).toPromise()
      .then(value => value.json() as Object)
      .catch(error => console.log(error))
  }

  addComment(commentAddDto: AddCommentDto): Promise<Object> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.addComment}`;
        return this.http.post(url, commentAddDto, {headers: new Headers({"session": sessionID})})
          .toPromise()
          .catch(error => console.log(error))
      });
  }

  deleteComment(postId: string, commentId: string): Promise<Object> {
    return this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.deleteComment}`;
        return this.http.delete(url, {
          headers: new Headers({"session": sessionID}),
          params: {comment_id: commentId, post_id: postId}
        }).toPromise()
          .catch(error => console.log(error))
      });
  }

  //rating = null then reset rating from user, rating = true rating up, rating = false then rating down
  changeCommentRating(postId: string, commentId: string, rating: boolean) {
    this.sessionService.getUserSession()
      .then(sessionID => {
        const url = `${environment.host}${environment.requests.changeCommentRating}`;
        this.http.post(url, null, {
          headers: new Headers({"session": sessionID}),
          params: {comment_id: commentId, post_id: postId, rating: rating}
        })
      });
  }

}
