import {Component} from "@angular/core";
import {UserInfo} from "../models/user-info.model";

@Component({
  selector: 'app-root',
  templateUrl: './../templates/app.template.html',
  styleUrls: ['./../css/app.style.css']
})
export class AppComponent {
  userInfo : UserInfo;
}
