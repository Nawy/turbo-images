import {Component, OnInit} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {UserImage} from "../../models/user-image.model";
import {Subject} from "rxjs/Subject";
import {Router} from "@angular/router";
/**
 * Created by ermolaev on 7/9/17.
 */


@Component({
  selector: "s-upload",
  templateUrl: './../../templates/uploads/upload.template.html',
  styleUrls: ['./../../css/upload.style.css'],
})
export class UploadComponent implements OnInit {
  images : Array<UserImage>;
  uploadedImagesCount: number;
  uploadedProgress: number;
  totalImageCount: number;

  tagsModel : string;

  tags : Array<string> = [];

  constructor(private imageService : ImageService, private router: Router) {
    if(this.imageService.uploadFiles == null) {
      this.router.navigateByUrl("/my-images");
    }
  }

  ngOnInit(): void {
    this.imageService.eventNewFilesIsReady.subscribe(value => {
      this.uploadImages();
    });
    this.uploadImages();
  }

  private uploadImages() {
    this.images = [];
    this.uploadedImagesCount = 0;
    this.uploadedProgress = 0;
    this.totalImageCount = this.imageService.uploadFiles.length;

    let oneImageInPercents = 100 / this.totalImageCount;
    for (let file of this.imageService.uploadFiles) {
      this.imageService.uploadImage(file)
        .then(userImage => {
          this.images.push(userImage);
          this.uploadedImagesCount++;
          this.uploadedProgress += oneImageInPercents;
        });
    }
    this.imageService.uploadFiles = null;
  }


  public updateTags() {
    this.tags = [];
    this.tagsModel.trim()
      .split(",")
      .forEach(value => {
        this.tags.push(value.trim());
      });
  }
}
