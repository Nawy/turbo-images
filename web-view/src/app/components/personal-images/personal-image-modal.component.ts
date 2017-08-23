/**
 * Created by ermolaev on 7/20/17.
 */
import {Component, Input} from "@angular/core";
import {UserImage} from "../../models/user-image.model";
import {environment} from "../../../environments/environment";
import {PersonalHolderService} from "../../service/personal-holder.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";


@Component({
  selector: "ngbd-modal-personal-image",
  templateUrl: './../../templates/personal-images/personal-image-modal.template.html',
  styleUrls: ['./../../css/personal-images/personal-image-modal.style.css']
})
export class PersonalImageModalComponent {
  userImage: UserImage;
  imageSource: string;

  constructor(private personalHolderService: PersonalHolderService,
              private route : Router,
              private activeModal: NgbActiveModal) {
    this.userImage = personalHolderService.personalImage;
    this.imageSource = `http://${environment.imageHost}${this.userImage.image.source}`;
  }

  close() {
    this.activeModal.close();
  }

  copyToClipBoard(object : any) {
    console.info(object);
    object.select();
    document.execCommand("copy");
  }

  goToImagePage() {
    this.activeModal.close();
    this.route.navigate(["/img", this.userImage.id]);
  }

}
