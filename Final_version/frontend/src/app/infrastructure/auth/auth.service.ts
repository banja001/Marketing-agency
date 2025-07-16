import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from 'src/env/environment';
import { Login, Passwordless } from 'src/app/model/login.model';
import { TokenStorage } from './jwt/token.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthenticationResponse } from 'src/app/model/authentication-response.model';
import { Registration } from 'src/app/model/registration.model';
import { User } from 'src/app/model/user.model';
import { Employee } from 'src/app/model/employee.model';
import { RecoverAccount } from 'src/app/model/change-password.model';
import { VerifyTotpDto } from 'src/app/model/totp.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user$ = new BehaviorSubject<User>({
    username: '',
    id: 0,
    role: '',
  });

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorage,
    private router: Router
  ) {}

  login(login: Login): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(environment.apiHost + 'auth/login', login)
      .pipe(
        tap((authenticationResponse) => {
          if (
            authenticationResponse.accessToken &&
            authenticationResponse.refreshToken
          ) {
            this.tokenStorage.saveAccessToken(
              authenticationResponse.accessToken
            );
            this.tokenStorage.saveRefreshToken(
              authenticationResponse.refreshToken
            );
            this.setUser();
          }
        })
      );
  }

  verifyTotp(verifyTotp: VerifyTotpDto): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(
        environment.apiHost + 'users/verifyTotp',
        verifyTotp
      )
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.tokenStorage.saveRefreshToken(
            authenticationResponse.refreshToken
          );
          this.setUser();
        })
      );
  }

  logout(): void {
    this.tokenStorage.clear();
    this.router.navigate(['']);
    this.user$.next({ username: '', id: 0, role: '' });
  }

  checkIfUserExists(): void {
    const accessToken = this.tokenStorage.getAccessToken();
    if (accessToken == null) {
      return;
    }
    this.setUser();
  }

  private setUser(): void {
    const jwtHelperService = new JwtHelperService();
    const accessToken = this.tokenStorage.getAccessToken() || '';
    const user: User = {
      id: +jwtHelperService.decodeToken(accessToken).id,
      username: jwtHelperService.decodeToken(accessToken).username,
      role: jwtHelperService
        .decodeToken(accessToken)
        .roles.substring(
          1,
          jwtHelperService.decodeToken(accessToken).roles.length - 1
        ),
    };
    this.user$.next(user);
  }

  getCurrentUserId(): number {
    const jwtHelperService = new JwtHelperService();
    const accessToken = this.tokenStorage.getAccessToken() || '';
    const decodedToken = jwtHelperService.decodeToken(accessToken);
    console.log(decodedToken.exp);

    return decodedToken.id;
  }

  register(registration: Registration): Observable<String> {
    return this.http.post<String>(
      environment.apiHost + 'auth/register',
      registration,
      { responseType: 'text' as 'json' }
    );
  }

  sendVerificationEmail(login: Passwordless): Observable<Boolean> {
    return this.http.put<Boolean>(
      environment.apiHost + 'auth/sendVerificationEmail',
      login
    );
  }

  verify(token: string): Observable<string> {
    return this.http.get(
      environment.apiHost + 'auth/passwordlessLogin/verify/' + token,
      { responseType: 'text' }
    );
  }

  passwordlessLogin(username: string): Observable<AuthenticationResponse> {
    return this.http
      .get<AuthenticationResponse>(
        environment.apiHost + 'auth/passwordlessLogin/' + username
      )
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
          this.tokenStorage.saveRefreshToken(
            authenticationResponse.refreshToken
          );
          this.setUser();
        })
      );
  }

  refreshToken(refreshToken: string): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(
        environment.apiHost + 'auth/refreshToken',
        refreshToken
      )
      .pipe(
        tap((response) => {
          this.tokenStorage.saveAccessToken(response.accessToken);
          this.tokenStorage.saveRefreshToken(response.refreshToken);
        })
      );
  }

  activateAccount(token: string): Observable<Boolean> {
    return this.http.get<Boolean>(
      environment.apiHost + 'auth/activateAccount/' + token
    );
  }

  sendForgottenPasswordMail(passwordless: Passwordless): Observable<Boolean> {
    return this.http.put<Boolean>(
      environment.apiHost + 'auth/sendForgottenPasswordMail/',
      passwordless
    );
  }

  recoverAccount(credentials: RecoverAccount): Observable<Boolean> {
    return this.http.put<Boolean>(
      environment.apiHost + 'auth/recoverAccount/',
      credentials
    );
  }

  deleteAccount(username: string): Observable<void> {
    return this.http.delete<void>(
      environment.apiHost + 'users/deleteAccount/' + username
    );
  }
}
