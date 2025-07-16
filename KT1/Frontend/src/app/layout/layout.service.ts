import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../env/environment';
import { KeyPair } from '../shared/model/keypair.model';
import {
  Certificate,
  CreateCertificate,
} from '../shared/model/certificate.model';
import { UserModel } from '../infrastructure/auth/model/user.model';

@Injectable({
  providedIn: 'root',
})
export class LayoutService {
  constructor(private http: HttpClient, private router: Router) {}

  createRoot(certificate: CreateCertificate): Observable<KeyPair> {
    return this.http.post<KeyPair>(
      environment.apiHost + 'cert/createRootCert',
      certificate
    );
  }

  getRoot(id: string): Observable<Certificate> {
    console.log('id');
    console.log(id);
    return this.http.get<Certificate>(
      environment.apiHost + 'cert/getRoot/' + id
    );
  }

  getImIssuers(): Observable<Certificate[]> {
    return this.http.get<Certificate[]>(
      environment.apiHost + 'cert/getImIssuers'
    );
  }

  getAllCertificates(): Observable<Certificate[]> {
    return this.http.get<Certificate[]>(
      environment.apiHost + 'cert/getCertificates'
    );
  }

  createCert(cert: CreateCertificate): Observable<KeyPair> {
    return this.http.post<KeyPair>(
      environment.apiHost + 'cert/createCert',
      cert
    );
  }

  revokeCertificate(cert: Certificate): Observable<Certificate> {
    return this.http.put<Certificate>(
      environment.apiHost + 'cert/revokeCert/' + cert.id,
      cert
    );
  }

  search(param: string): Observable<boolean> {
    return this.http.get<boolean>(environment.apiHost + 'cert/search/' + param);
  }
}
