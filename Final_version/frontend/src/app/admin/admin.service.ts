import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../model/user.model';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Registration } from '../model/registration.model';
import { environment } from 'src/env/environment';
import { TokenStorage } from '../infrastructure/auth/jwt/token.service';
import { UserProfile } from '../model/user-profile.model';
import { Notification } from 'src/app/model/notification';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(
    private http: HttpClient,
    private router: Router,
    private token: TokenStorage
  ) {}

  getRequests(): Observable<Registration> {
    return this.http.get<Registration>(
      environment.apiHost + 'users/inactiveClients'
    );
  }

  sendMail(username: string): Observable<Boolean> {
    return this.http.get<Boolean>(
      environment.apiHost + 'users/sendMail/' + username
    );
  }

  blockRequest(username: string, reason: string): Observable<Boolean> {
    return this.http.post<Boolean>(
      environment.apiHost + 'users/blockRequest/' + username,
      reason
    );
  }

  getNotifications(userId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(
      `https://localhost:8090/api/notifications/${userId}`
    );
  }

  blockUser(user: UserProfile): Observable<Boolean> {
    return this.http.post<Boolean>(
      environment.apiHost + 'users/blockUser/' + user.username,
      user
    );
  }
}
