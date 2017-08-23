import {Component, HostListener} from "@angular/core";
import {UserInfo} from "../models/user-info.model";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './../templates/app.template.html',
  styleUrls: ['./../css/app.style.css']
})
export class AppComponent {
  userInfo : UserInfo;
  modalWindow : NgbModalRef = null;

  constructor(private modalService: NgbModal) {}

  dropImage(event : any, content : any) {
    event.preventDefault();
    content.close();
    console.info("UPLOOOOOAAAD~!");
  }

  // @HostListener('window:dragenter', ['$event'])
  // public dragoverImage(event : any, content : any) {
  //   event.preventDefault();
  //   console.info(content);
  //   if (this.modalWindow == null) {
  //     this.modalWindow = this.modalService.open(content);
  //     console.info("DRAGG OVER");
  //   }
  // }
}
