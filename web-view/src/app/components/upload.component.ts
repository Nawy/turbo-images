import {Component} from "@angular/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
/**
 * Created by ermolaev on 7/9/17.
 */


@Component({
  selector: "s-upload",
  templateUrl: './../templates/upload.template.html',
  styleUrls: ['./../css/upload.style.css'],
})
export class UploadComponent {

  constructor(private modalService: NgbModal) {}

  open(content) {
    this.modalService.open(content, { windowClass: 'dark-modal' });
  }

  uploadImage(event : any) {
    event.preventDefault();
    console.info("UPLOOOOOAAAD~!");
  }

  allowCheck(event : any) {
    event.preventDefault();
    console.info("DRAGG OVER");
  }
}
