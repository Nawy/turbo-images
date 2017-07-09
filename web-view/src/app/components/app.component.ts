import {Component} from "@angular/core";
import {UserInfo} from "../models/user-info.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './../templates/app.template.html',
  styleUrls: ['./../css/app.style.css']
})
export class AppComponent {
  userInfo : UserInfo;

  constructor(private modalService: NgbModal) {}

  dropImage(event : any, content : any) {
    event.preventDefault();
    content.close();
    console.info("UPLOOOOOAAAD~!");
  }

  dragoverImage(event : any, content : any) {
    event.preventDefault();
    console.info(content);
    this.modalService.open(content);
    console.info("DRAGG OVER");
  }
}
