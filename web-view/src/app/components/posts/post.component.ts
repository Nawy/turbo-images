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

@Component({
  selector: "s-post",
  templateUrl: '../../templates/posts/post.template.html',
  styleUrls: ['../../css/post.style.css']
})
export class PostComponent implements OnInit {

  userInfo: UserInfo;
  post: Post;
  tags: Array<string> = [];

  constructor(private route: ActivatedRoute,
              private location: Location,
              private postService: PostService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    window.scrollTo(0, 0);
    this.userService.userInfoSource.subscribe(userInfo => {
      this.userInfo = userInfo;
    });
    if (this.userInfo == null) {
      this.userService.updateUserInfo();
    }
    this.route.params.subscribe(
      params => {
        const id = params['id'];
        this.postService.getPost(id)
          .then(post => this.post = post)
      }
    )
  }

  isReadonly() {
    return !this.post || !this.userInfo || this.post.user_id != this.userInfo.id;
  }

  goBack(): void {
    this.location.back();
  }

  show(){
    this.post.visible=true;
    this.postService.savePost(
      TransferPost.makeTransferPost(this.post)
    );
  }

  hide(){
    this.post.visible=false;
    this.postService.savePost(
      TransferPost.makeTransferPost(this.post)
    );
  }
}
