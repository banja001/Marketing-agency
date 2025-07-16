import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../env/environment';
import { LoginCredentials } from './model/login-credentials.model';
import { User, UserModel } from './model/user.model';
import { AuthenticationResponse } from './model/authentication-response.model';
import { TokenStorage } from './jwt/token.service';
import { JwtHelperService } from '@auth0/angular-jwt';

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

  verify(id: String): Observable<Boolean> {
    return this.http.get<Boolean>(environment.apiHost + 'auth/verify/' + id);
  }

  login(login: LoginCredentials): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(environment.apiHost + 'auth/login', login)
      .pipe(
        tap((authenticationResponse) => {
          this.tokenStorage.saveAccessToken(authenticationResponse.accessToken);
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
      role: jwtHelperService.decodeToken(accessToken).role,
    };

    this.user$.next(user);
  }

  getCurrentUserId(): number {
    const jwtHelperService = new JwtHelperService();
    const accessToken = this.tokenStorage.getAccessToken() || '';
    const decodedToken = jwtHelperService.decodeToken(accessToken);

    return decodedToken.id;
  }

  getUserByUsername(username: String): Observable<UserModel> {
    return this.http.get<UserModel>(
      environment.apiHost + 'auth/user/' + username
    );
  }
}
