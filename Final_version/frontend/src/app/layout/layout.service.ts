import { Injectable } from '@angular/core';
import { User } from '../model/user.model';
import { Observable } from 'rxjs';
import { environment } from 'src/env/environment';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserProfile } from '../model/user-profile.model';
import { ChangePassword } from '../model/change-password.model';
import { UserCreation } from '../model/user-creation.model';
import { RequestCreation } from '../model/request-creation.model';
import { Request } from '../model/request.model';
import { Commercial } from '../model/commercial.model';
import { Employee } from '../model/employee.model';
import { TokenStorage } from '../infrastructure/auth/jwt/token.service';
import { VPNMessage } from '../model/VPNMessage';

@Injectable({
  providedIn: 'root',
})
export class LayoutService {
  constructor(private http: HttpClient, private router: Router, private token: TokenStorage) {}

  getUserProfile(id: number): Observable<UserProfile> {
    return this.http.get<UserProfile>(environment.apiHost + 'users/' + id);
  }

  editUserProfile(profile: UserProfile): Observable<UserProfile> {
    return this.http.put<UserProfile>(
      environment.apiHost + 'users/edit',
      profile
    );
  }

  changePassword(changePassword: ChangePassword): Observable<Boolean> {
    return this.http.put<Boolean>(
      environment.apiHost + 'users/changePassword',
      changePassword
    );
  }

  getAllUsers(roleName: string): Observable<UserProfile[]> {
    return this.http.get<UserProfile[]>(
      environment.apiHost + 'users/role/' + roleName
    );
  }

  createUser(user: UserCreation): Observable<UserCreation> {
    const requestBody = {
      param1: user,
      param2: this.token.getAccessToken()
    };
    return this.http.post<UserCreation>(
      environment.apiHost + 'users/create',
      requestBody
    );
  }

  createRequest(request: RequestCreation): Observable<RequestCreation> {
    return this.http.post<RequestCreation>(
      environment.apiHost + 'users/request/create',
      request
    );
  }

  getRequests(): Observable<Request[]> {
    return this.http.get<Request[]>(environment.apiHost + 'users/requests');
  }

  createCommercial(commercial: Commercial): Observable<Commercial> {
    return this.http.post<Commercial>(
      environment.apiHost + 'users/commercial/create',
      commercial
    );
  }

  deleteRequest(requestId: string): Observable<void> {
    return this.http.delete<void>(
      environment.apiHost + 'users/requests/delete/' + requestId
    );
  }

  getCommercials(): Observable<Commercial[]> {
    return this.http.get<Commercial[]>(
      environment.apiHost + 'users/commercials'
    );
  }

  getEmployee(id: number): Observable<Employee> {
    return this.http.get<Employee>(
      environment.apiHost + 'users/employee/' + id
    );
  }

  getVPN(): Observable<VPNMessage> {
    return this.http.get<VPNMessage>(
      environment.apiHost + 'vpn'
    );
  }
}
