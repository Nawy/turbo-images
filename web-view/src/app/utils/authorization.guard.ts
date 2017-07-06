import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs/Observable";
import {UserService} from "../service/user.service";
import {Injectable} from "@angular/core";
import {UserInfo} from "../models/user-info.model";
/**
 * Created by ermolaev on 7/6/17.
 */

@Injectable()
export class AuthorizationGuard implements CanActivate {

  constructor (
    private router : Router,
    private userService : UserService
  ) {
  }


  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {

    if(this.userService.isLoggedIn()) {
      console.error("USER IS LOGGED!");
      this.router.navigateByUrl("/");
      return false;
    }
    return true;
  }
}
