import {environment} from "../../../environments/environment";
import {Component} from "@angular/core";
import {PersonalHolderService} from "../../service/personal-holder.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {UserImage} from "../../models/user-image.model";
/**
 * Created by ermolaev on 7/23/17.
 */

@Component({
  selector: "ngbd-modal-personal-image",
  templateUrl: './../../templates/personal-images/personal-image-modal.template.html',
  styleUrls: ['./../../css/personal-images/personal-image-modal.style.css']
})
export class ImagePageComponent {
  userImage : UserImage;
  imageSource : string;

  constructor(private personalHolderService: PersonalHolderService) {
    this.userImage = personalHolderService.personalImage;
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }
}
