import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, switchMap, tap, throwError } from 'rxjs';
import { AuthService } from '../auth.service';
import { TokenStorage } from './token.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthenticationResponse } from 'src/app/model/authentication-response.model';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorage,
    private jwtHelper: JwtHelperService
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const accessToken = this.tokenStorage.getAccessToken();
    const refreshToken = this.tokenStorage.getRefreshToken();
    console.log(
      'ACCESS: ',
      this.jwtHelper.getTokenExpirationDate(accessToken || '')
    );

    console.log('ACCESS EXPIRED: ', this.jwtHelper.isTokenExpired(accessToken));
    console.log('ACCESS: ', accessToken);

    if (accessToken && !this.jwtHelper.isTokenExpired(accessToken)) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
    }

    return next.handle(request).pipe(
      tap({
        next: () => null,
        error: (err: HttpErrorResponse) => {
          if ([401, 403].includes(err.status)) {
            console.log('NEKI ERROR!');
            const refreshToken = this.tokenStorage.getRefreshToken();
            return this.authService.refreshToken(refreshToken || '').pipe(
              tap({
                next: (response) => {
                  console.log('access: ', response.accessToken);
                  this.tokenStorage.saveAccessToken(response.accessToken);
                  this.tokenStorage.saveRefreshToken(response.refreshToken);

                  const newRequest = request.clone({
                    setHeaders: {
                      Authorization: `Bearer ${response.accessToken}`,
                    },
                  });
                  return next.handle(newRequest);
                },
                error: (error) => {
                  console.log(error);
                },
              })
            );
          }
          return throwError(err);
        },
      })
    );
  }
}
