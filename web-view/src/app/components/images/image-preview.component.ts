/**
 * Created by ermolaev on 5/14/17.
 */

import {Component, Input} from "@angular/core";
import {UserImage} from "../../models/user-image.model";
import {environment} from "../../../environments/environment";
import {PersonalImageModalComponent} from "./personal-image-modal.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PersonalHolderService} from "../../service/personal-holder.service";
import * as moment from 'moment';

@Component({
  selector: "s-image-preview",
  templateUrl: '../../templates/images/image-preview.template.html'
})
export class ImagePreviewComponent {
  @Input("user_image") userImage: UserImage;

  //style
  activeClass: string;
  defaultClass: string;
  useClass: string;

  constructor(private personalHolderService: PersonalHolderService,
              private modalService: NgbModal) {
    this.activeClass = "postpreview-active";
    this.defaultClass = "postpreview-default";

    this.useClass = this.defaultClass;
  }

  showImage() {
    this.personalHolderService.personalImage = this.userImage;
    this.modalService.open(PersonalImageModalComponent, { size: "lg"});
  }

  getThumbnail(): string {
    return `http://${environment.imageHost}${this.userImage.image.thumbnail}`
  }

  getHeaderString(): string {
    return "Uploaded at " + moment(this.userImage.creation_date).format("HH:mm");
  }

  onMouseOver(): void {
    this.useClass = this.activeClass;
  }

  onMouseOut(): void {
    this.useClass = this.defaultClass;
  }

  getTitle() {
    return this.userImage.name
  }
}
