/**
 * Created by ermolaev on 5/9/17.
 */
import {Component, HostListener} from "@angular/core";
import {environment} from "../../../environments/environment";
import {UserPostsMap} from "../../models/user-posts-map.model";
import * as moment from 'moment';
import * as Rx from "rxjs"
import {PostService} from "../../service/post.service";
import {SearchSort} from "../../models/search-sort.model";
import {SearchPeriod} from "../../models/search-period.model";

@Component({
  templateUrl: '../../templates/posts/all-posts.template.html',
  styleUrls: ['../../css/all-posts.style.css']
})
export class AllPostsComponent {

  postsMap: Array<UserPostsMap> = [];
  isLoaderVisible: boolean = false;
  isAllPostsUploaded: boolean = false;

  searchSort: SearchSort = SearchSort.RATING;
  period: SearchPeriod = SearchPeriod.DAY;
  page: number = 0;

  constructor(private postService: PostService) {
  }

  ngOnInit(): void {
    this.uploadPostsByDate(new Date(Date.now()));
  }

  private uploadPostsByDate(startDate: Date) {
    this.isLoaderVisible = true;
    this.postService
      .getPosts(startDate)
      .then(posts => {

        if (posts.length < environment.pageSize) {
          this.isAllPostsUploaded = true;
        }

        Rx.Observable.from(posts)
          .groupBy(
            post => moment(post.create_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate().getDate()
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

  getClassState(name: string): string {
    if (name.match("RATING") && this.searchSort === SearchSort.RATING) {
      return "active";
    }

    if (name.match("VIEW") && this.searchSort === SearchSort.VIEW) {
      return "active";
    }

    if (name.match("NEW") && this.searchSort === SearchSort.NEW) {
      return "active";
    }

    return "";
  }

  chooseTopRatingState(): void {
    this.searchSort = SearchSort.RATING;
  }

  chooseTopViewState(): void {
    this.searchSort = SearchSort.VIEW;
  }

  chooseNewState(): void {
    this.searchSort = SearchSort.NEW;
  }
}
