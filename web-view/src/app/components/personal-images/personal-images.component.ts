import {Component} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {UserImage} from "../../models/user-image.model";
import * as moment from 'moment';
import * as Rx from "rxjs"

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
    if(this.creationDate.getDate() == new Date().getDate()) {
      return "Today";
    }
    return moment(this.creationDate).format("D MMMM");
  }
}

@Component({
  templateUrl: './../../templates/personal-images/personal-images.template.html',
  styleUrls: ['./../../css/personal-images.style.css']
})
export class PersonalImagesComponent {
  images: Array<UserImage>;
  imagesMap: Array<UserImagesMap> = [];

  constructor(private imageService: ImageService) {
    imageService.getUserImages().then(images => {
      Rx.Observable.from(images)
        .groupBy(
          x => moment(x.creation_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate().getDay()
        )
        .flatMap(group => {
          console.info(group);
          return group.reduce((acc, curr) => [...acc, curr], []);
        })
        .map(values => {
          return new UserImagesMap(moment(values[0].creation_date, "YYYY-MM-DD HH:mm:ss.SSS").toDate(), values);
        }).forEach(value => this.imagesMap.push(value));

    });
  }
}
