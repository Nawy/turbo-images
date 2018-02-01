import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PostService} from "../../service/post.service";
import {Location} from "@angular/common";
import {PostPreview} from "../../models/post/post-preview.model";

@Component({
  templateUrl: '../../templates/search/post-search.template.html'
})
export class PostSearchComponent implements OnInit {
  query: string;
  posts : Array<PostPreview>;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private postService: PostService) {
  }

  ngOnInit(): void {
    window.scrollTo(0, 0);
    const query = this.route.snapshot.queryParams['q'];
    console.info("VALUE: " + query);
    this.query = query;
    this.postService.findPost(query)
      .then(posts => {
        this.posts = posts;
      });
  }
}
