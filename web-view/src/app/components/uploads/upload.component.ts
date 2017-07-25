import {Component} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {UserImage} from "../../models/user-image.model";
import {Subject} from "rxjs/Subject";
/**
 * Created by ermolaev on 7/9/17.
 */


@Component({
  selector: "s-upload",
  templateUrl: './../../templates/uploads/upload.template.html',
  styleUrls: ['./../../css/upload.style.css'],
})
export class UploadComponent {

  images : Array<UserImage>;
  private uploadedImage = new Subject<UserImage>();

  constructor(private imageService : ImageService) {
    this.images = [];
    this.uploadedImage.subscribe(userImage => this.images.push(userImage));

    for (var file of imageService.uploadFiles) {
      this.imageService.uploadImage(file).then(userImage => this.uploadedImage.next(userImage));
    }


    // let img1 = new UserImage();
    // img1.id = "195359179";
    // img1.description = "Парам парам пам";
    // img1.create_date = "2017-07-13 12:07";
    // img1.image = new Image();
    // img1.image.thumbnail = "/t/R/khGvIaxECx.jpg";
    // img1.image.source = "/i/e/8hmnHGVDUV.jpg";
    //
    // let img2 = new UserImage();
    // img2.id = "195359179";
    // img2.description = "Описание этого всего 2";
    // img2.create_date = "2017-07-13 12:07";
    // img2.image = new Image();
    // img2.image.thumbnail = "/t/j/9ho9C0QPfz7.jpg";
    // img2.image.source = "/i/x/Yh14sQXvfb.jpg";
    //
    // let img3 = new UserImage();
    // img3.id = "195359179";
    // img3.description = "fsdfsd";
    // img3.create_date = "2017-07-13 12:07";
    // img3.image = new Image();
    // img3.image.thumbnail = "/t/4/ohD0hnegUY.jpg";
    // img3.image.source = "/i/3/oh0ESrA5t83.jpg";
    //
    // this.images.push(img1);
    // this.images.push(img2);
    // this.images.push(img3);
  }

}
