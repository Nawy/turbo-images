import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Component} from "@angular/core";
import {ImageService} from "../../service/image.service";
import {Router} from "@angular/router";

/**
 * Created by ermolaev on 7/16/17.
 */
@Component({
  selector: "ngbd-modal-upload",
  templateUrl: './../../templates/uploads/upload-modal.template.html'
})
export class UploadModalComponent {

  constructor(
    private activeModal: NgbActiveModal,
    private imageService: ImageService,
    private router: Router) {}

  close() {
    this.activeModal.close();
  }

  dropImage(event : any) {
    console.debug("DROP");
    event.preventDefault();
    event.stopPropagation();
    this.uploadImage(
      event.dataTransfer.files
    );
  }

  changeImageInput(event:any){
    this.uploadImage(
      event.target.files
    );
  }

  uploadImage(images:Array<File>){
    this.imageService.uploadFiles = images;
    this.activeModal.close();
    console.info("Current: "  + this.router.url);
    if(this.router.url == "/uploads") {
      this.imageService.eventNewFilesIsReady.next(true);
      this.activeModal.close();
    } else {
      this.router.navigateByUrl("uploads");
    }
  }



  dragoverImage(event : any) {
    event.preventDefault();
    event.stopPropagation();
    console.debug("DRAGG OVER");
  }
}
