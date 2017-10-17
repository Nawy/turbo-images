/**
 * Created by ermolaev on 7/13/17.
 */
import {Component, Input, OnInit} from "@angular/core";
import {UserImage} from "../../models/user-image.model";
import {environment} from "../../../environments/environment";
import {ImageService} from "../../service/image.service";

/**
 * Created by ermolaev on 7/9/17.
 */

@Component({
  selector: "s-upload-preview",
  templateUrl: './../../templates/uploads/upload-preview.template.html',
  styleUrls: ['./../../css/upload-preview.style.css'],
})
export class UploadPreviewComponent implements OnInit {
  @Input("user_image") userImage: UserImage;
  private imageSource: string;

  private autoUpdateInterval: number = environment.autoUpdateInterval;
  private updateTimer;

  constructor(private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }

  deferredTitleUpdate() {
    clearTimeout(this.updateTimer);
    this.updateTimer = setTimeout(
      () => this.saveTitle(),
      this.autoUpdateInterval
    );
  }

  deferredDescriptionUpdate() {
    clearTimeout(this.updateTimer);
    this.updateTimer = setTimeout(
      () => {this.saveDescription(); console.log("Description saved!")},
      this.autoUpdateInterval
    );
  }

  saveDescription() {
    this.imageService
      .editUserImageDescription(this.userImage.id, this.userImage.description)
      .then(userImage => this.userImage = userImage);
  }

  saveTitle() {
    this.imageService
      .editUserImageName(this.userImage.id, this.userImage.name)
      .then(userImage => this.userImage = userImage);
  }

  //TODO work only for first image! Fix!!!
  autoGrow() {
    let element: HTMLElement = document.getElementById("desc");
    element.style.height = "5px";
    element.style.height = (element.scrollHeight) + "px";
  }
}
