/**
 * Created by ermolaev on 5/14/17.
 */
import {Component, HostListener} from '@angular/core'
import * as moment from 'moment';
import * as Rx from "rxjs"
import {PostService} from "../../service/post.service";
import {environment} from "environments/environment";
import {UserPostsMap} from "../../models/user-posts-map.model";

@Component({
  templateUrl: '../../templates/posts/personal-posts.template.html'
})
export class PersonalPostsComponent {
  postsMap: Array<UserPostsMap> = [];
  isLoaderVisible: boolean = false;
  isAllPostsUploaded: boolean = false;

  constructor(private postService: PostService) {
  }

  ngOnInit(): void {
    this.uploadPostsByDate(new Date(Date.now()));
  }

  private uploadPostsByDate(startDate: Date) {
    this.isLoaderVisible = true;
    this.postService.getUserPosts(startDate).then(posts => {

      if (posts.length < environment.pageSize) {
        this.isAllPostsUploaded = true;
      }

      Rx.Observable.from(posts)
        .groupBy(
          post => moment(post.create_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate().toDateString()
        )
        .flatMap(group =>
          group.reduce((acc, curr) => [...acc, curr], [])
        )
        .map(posts =>
          new UserPostsMap(moment(posts[0].create_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate(), posts)
        )
        .forEach(post => this.postsMap.push(post));
      this.isLoaderVisible = false
    })
  }

  @HostListener('window:scroll', ['$event'])
  public onScroll(event: Event) {
    if (this.isAllPostsUploaded || this.isLoaderVisible) {
      return;
    }

    if (window.innerHeight + window.scrollY === document.body.scrollHeight) {
      this.uploadPostsByDate(this.getLastPostDate());
    }
  }

  private getLastPostDate(): Date {
    let lastPartOfDay = this.postsMap[this.postsMap.length - 1];
    let stringDate = lastPartOfDay.posts[lastPartOfDay.posts.length - 1].create_date;
    return moment(stringDate, "YYYY-MM-DD HH:mm:ss.SSS").toDate();
  }
}
