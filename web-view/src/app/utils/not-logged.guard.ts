import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {UserService} from "../service/user.service";
/**
 * Created by ermolaev on 7/7/17.
 */

@Injectable()
export class NotLoggedGuard implements CanActivate {

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
      return true;
    }

    this.router.navigateByUrl("/signin");
    return false;
  }
}
