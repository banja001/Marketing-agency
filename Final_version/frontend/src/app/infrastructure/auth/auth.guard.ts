import { CanActivate, CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user.model';

export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {}

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const user: User = this.authService.user$.getValue();
    if (user.username === '') {
      this.router.navigate(['']);
      return false;
    }
    return true;
  }
}
