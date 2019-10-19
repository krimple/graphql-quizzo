import {Injectable} from '@angular/core';
import {Router, CanActivate, RouterStateSnapshot, ActivatedRouteSnapshot} from '@angular/router';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class JwtAuthguardService implements CanActivate {

  constructor(private router: Router, private authService: AuthService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
   if (!this.authService.token) {
     const candidateToken = localStorage.getItem('token');
     if (candidateToken) {
       this.authService.token = candidateToken;
     }
     console.log('No token! Forcing login!');
     this.router.navigate(['login'], { queryParams: { returnUrl: state.url}});
     return false;
   } else {
     // ok, we have a token. Let the network intercept for an HttpRequest rip it out if invalid...
     return true;
   }
 }
}
