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
}
