import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Auth } from '../services/auth';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationGuard implements CanActivate {

  constructor(private authService:Auth,private router :Router) {

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
if(this.authService.roles.includes("ADMIN")){
  return true;
}
else {

this.router.navigateByUrl("/admin/notAuthorized")
  return false;
  }}
}

