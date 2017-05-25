/**
 * Created by ermolaev on 5/9/17.
 */

import {Component} from '@angular/core'
import { Router } from '@angular/router';

@Component({
  selector: "s-navbar",
  templateUrl: './../templates/navbar.template.html',
  styleUrls: ['./../css/navbar.style.css']
})
export class NavbarComponent {

  constructor(private router: Router) {}

  logout(): void {
    this.router.navigateByUrl("login");
  }
}
