/**
 * Created by ermolaev on 5/9/17.
 */

import {Component, OnInit} from '@angular/core'
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {UserInfo} from "../../models/user-info.model";
import {Post} from "../../models/post/post.model";
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {TransferPost} from "../../models/post/add-post-dto.model";
import {isNullOrUndefined} from "util";
import {environment} from "../../../environments/environment";

@Component({
  selector: "s-post",
  templateUrl: '../../templates/posts/post.template.html',
  styleUrls: ['../../css/post.style.css']
})
export class PostComponent implements OnInit {

  userInfo: UserInfo;
  post: Post;
  updateTimer;
  autoUpdateInterval: number = environment.autoUpdateInterval;
  tagsString: string = "";

  constructor(private route: ActivatedRoute,
              private location: Location,
              private postService: PostService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.userService.userInfoSource.subscribe(userInfo => {
      this.userInfo = userInfo;
      // this required because autogrow work only if window component is loaded and filled with data
      setTimeout(() => this.autoGrow(), 20);
    });
    if (this.userInfo == null) this.userService.updateUserInfo();
    this.route.params.subscribe(
      params => {
        const id = params['id'];
        this.postService.getPost(id)
          .then(post => {
            this.post = post;
            if (post.tags.length > 0) {
              this.tagsString = post.tags.join();
              console.log("tags string: " + this.tagsString);
            }
          });
      }
    );
  }

  isReadonly(): boolean {
    return !(this.post && this.userInfo && this.post.user_id == this.userInfo.id);
  }

  goBack(): void {
    this.location.back();
  }

  deferredPostUpdate() {
    clearTimeout(this.updateTimer);
    this.updateTimer = setTimeout(
      () => this.savePost(this.post),
      this.autoUpdateInterval
    );
  }

  autoGrow() {
    let elements: any = document.getElementsByClassName("post-description");

    for (let element of elements) {
      element.style.height = "5px";
      element.style.height = (element.scrollHeight) + "px";
    }
  }

  public updateTags() {
    const tags = [];
    this.tagsString.trim()
      .split(",")
      .forEach(tagValue => {
        if (tagValue.length > 0) {
          tags.push(tagValue.trim());
        }
      });
    this.post.tags = tags;
  }

  show() {
    this.post.visible = true;
    this.savePost(this.post);
  }

  hide() {
    this.post.visible = false;
    this.savePost(this.post);
  }

  canView(field: string): boolean {
    return (!isNullOrUndefined(field) && field.length > 0) || !this.isReadonly();
  }

  savePost(post: Post) {
    console.log("Post saved");
    this.postService.savePost(
      TransferPost.makeTransferPost(post)
    )
  }
}
