import {Component, HostListener, OnInit} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {UserImage} from "../../models/user-image.model";
import * as moment from 'moment';
import * as Rx from "rxjs"
import {environment} from "../../../environments/environment";

/**
 * Created by ermolaev on 7/11/17.
 */

class UserImagesMap {
  creationDate : Date;
  images: Array<UserImage>;


  constructor(creationDate: Date, images: Array<UserImage>) {
    this.creationDate = creationDate;
    this.images = images;
  }

  getCreationDate() : string {
    let currentDate = new Date().toDateString();
    if(this.creationDate.toDateString() == currentDate) {
      return "Today";
    }
    if(moment(this.creationDate).add(1, 'd').toDate().toDateString() == currentDate) {
      return "Yesterday";
    }
    return moment(this.creationDate).format("D MMMM");
  }
}

@Component({
  templateUrl: '../../templates/images/personal-images.template.html',
  styleUrls: ['../../css/personal-images.style.css']
})
export class PersonalImagesComponent implements OnInit {
  imagesMap: Array<UserImagesMap> = [];
  isLoaderVisible: boolean = false;
  isAllImageUploaded: boolean = false;

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {
    this.uploadImagesByDate(new Date(Date.now()));
  }

  private uploadImagesByDate(startDate : Date) {
    this.isLoaderVisible = true;
    this.imageService.getUserImages(startDate).then(images => {

      if(images.length < environment.uploadPersonImage.pageSize) {
        this.isAllImageUploaded = true;
      }

      Rx.Observable.from(images)
        .groupBy(
          image => moment(image.creation_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate().toDateString()
        )
        .flatMap(group => {
          return group.reduce((acc, curr) => [...acc, curr], []);
        })
        .map(values => {
          return new UserImagesMap(moment(values[0].creation_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate(), values);
        })
        .forEach(value => this.imagesMap.push(value));
        this.isLoaderVisible = false
    })
  }

  @HostListener('window:scroll', ['$event'])
  public onScroll(event: Event) {
    if(this.isAllImageUploaded || this.isLoaderVisible) {
      return;
    }

    if (window.innerHeight + window.scrollY === document.body.scrollHeight) {
      this.uploadImagesByDate(this.getLastImageDate());
    }
  }

  private getLastImageDate() : Date {
    let lastPartOfDay = this.imagesMap[this.imagesMap.length-1];
    let stringDate = lastPartOfDay.images[lastPartOfDay.images.length-1].creation_date;
    return moment(stringDate, "YYYY-MM-DD HH:mm:ss.SSS").toDate()
  }
}
