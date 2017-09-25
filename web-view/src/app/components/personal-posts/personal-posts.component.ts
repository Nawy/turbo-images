/**
 * Created by ermolaev on 5/14/17.
 */
import {Component} from '@angular/core'
import {PostPreview} from "../../models/post-preview.model";
import * as moment from 'moment';
import * as Rx from "rxjs"

class UserPostsMap {
  creationDate: Date;
  posts: Array<PostPreview>;


  constructor(creationDate: Date, posts: Array<PostPreview>) {
    this.creationDate = creationDate;
    this.posts = posts;
  }

  getCreationDate(): string {
    let currentDate = new Date().getDate();
    if (this.creationDate.getDate() == currentDate) {
      return "Today";
    }
    if (moment(this.creationDate).add(1, 'd').toDate().getDate() == currentDate) {
      return "Yesterday";
    }
    return moment(this.creationDate).format("D MMMM");
  }
}

@Component({
  templateUrl: '../../templates/personal-posts.template.html'
})
export class PersonalPostsComponent {
  imagesMap: Array<PostPreview> = [];
  isLoaderVisible: boolean = false;
  isAllImageUploaded: boolean = false;

  ngOnInit(): void {
    this.uploadImagesByDate(new Date(Date.now()));
  }

  private uploadImagesByDate(startDate: Date) {
    this.isLoaderVisible = true;
    /*this.imageService.getUserImages(startDate).then(images => {

      if(images.length < environment.pageSize) {
        this.isAllImageUploaded = true;
      }

      Rx.Observable.from(images)
        .groupBy(
          image => moment(image.creation_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate().getDate()
        )
        .flatMap(group => {
          return group.reduce((acc, curr) => [...acc, curr], []);
        })
        .map(values => {
          return new UserImagesMap(moment(values[0].creation_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate(), values);
        })
        .forEach(value => this.imagesMap.push(value));
      this.isLoaderVisible = false
    })*/
  }

  /*@HostListener('window:scroll', ['$event'])*/
  public onScroll(event: Event) {
    if (this.isAllImageUploaded || this.isLoaderVisible) {
      return;
    }

    if (window.innerHeight + window.scrollY === document.body.scrollHeight) {
      this.uploadImagesByDate(this.getLastImageDate());
      console.debug('scroll at bottom');
    }
  }

  private getLastImageDate(): Date {
    /*let lastPartOfDay = this.imagesMap[this.imagesMap.length-1];
    let stringDate = lastPartOfDay.images[lastPartOfDay.images.length-1].creation_date;
    return moment(stringDate, "YYYY-MM-DD HH:mm:ss.SSS").toDate()*/
    //FIXME just stub
    return new Date();
  }
}
