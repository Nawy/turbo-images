import {Component, OnInit} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {UserImage} from "../../models/user-image.model";
import {Router} from "@angular/router";
import {TransferPost} from "../../models/post/add-post-dto.model";
import {PostService} from "../../service/post.service";

/**
 * Created by ermolaev on 7/9/17.
 */


@Component({
  selector: "s-upload",
  templateUrl: './../../templates/uploads/upload.template.html',
  styleUrls: ['./../../css/upload.style.css'],
})
export class UploadComponent implements OnInit {
  images: Array<UserImage>;
  uploadedImagesCount: number;
  uploadedProgress: number;
  totalImageCount: number;

  newPost: TransferPost = TransferPost.emptyTransferPost();
  tagsString: string;

  tags: Array<string> = [];

  constructor(private imageService: ImageService,
              private postService: PostService,
              private router: Router) {
    if (this.imageService.uploadFiles == null) {
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
    this.tagsString.trim()
      .split(",")
      .forEach(tagValue => {
        if (tagValue.length > 0) {
          this.tags.push(tagValue.trim());
        }
      });
    this.newPost.tags = this.tags;
  }

  public shareWithCommunity() {
    console.info("Pressed!");
    this.newPost.setImages(this.images);
    this.postService.savePost(this.newPost)
      .then(post => {
        this.router.navigateByUrl("post/" + post.id)
      });
  }
}
