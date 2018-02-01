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
    this.route.queryParams.subscribe(params => this.findPost(params['q']));
  }

  findPost(query : string) {
    this.query = query;
    console.info("QUERY: " + query);
    if(query) {
      this.postService
        .findPost(query)
        .then(posts => {
          this.posts = posts;
        });
    }
  }
}
