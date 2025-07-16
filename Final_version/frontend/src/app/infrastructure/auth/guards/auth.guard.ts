import { inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthService } from '../auth.service';

export function authGuard(...roles: String[]): CanActivateFn {
  return (ars: ActivatedRouteSnapshot, rss: RouterStateSnapshot) => {
    if (inject(AuthService).user$.getValue().username === '') {
      inject(Router).navigate(['login']);
      return false;
    } else if (
      roles.length !== 0 &&
      !roles.includes(inject(AuthService).user$.getValue().role)
    ) {
      inject(Router).navigate(['/profile']);
      return false;
    } else {
      return true;
    }
  };
}
