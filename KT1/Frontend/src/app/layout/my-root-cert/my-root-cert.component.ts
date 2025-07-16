import { Component } from '@angular/core';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { NavigationCancel, Router } from '@angular/router';
import { KeyPair } from '../../shared/model/keypair.model';
import {
  CreateCertificate,
  Certificate,
} from '../../shared/model/certificate.model';
import { User } from '../../infrastructure/auth/model/user.model';
import { LayoutService } from '../layout.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-my-root-cert',
  templateUrl: './my-root-cert.component.html',
  styleUrl: './my-root-cert.component.css',
})
export class MyRootCertComponent {
  pubKey: string = '';
  privKey: string = '';
  user: User | undefined;
  showCreateButton: Boolean = true;
  certificate: CreateCertificate = {
    subjectUsername: '',
    issuerUsername: '',
    publicKey: '',
    privateKey: '',
  };

  myCertificate: Certificate = {
    id: NaN,
    subject: {
      id: NaN,
      name: '',
      lastname: '',
      email: '',
      username: '',
      phoneNumber: '',
      password: '',
      role: NaN,
      organization: '',
      country: '',
    },
    issuer: {
      id: NaN,
      name: '',
      lastname: '',
      email: '',
      username: '',
      phoneNumber: '',
      password: '',
      role: NaN,
      organization: '',
      country: '',
    },
    startDate: new Date(),
    endDate: new Date(),
    type: NaN,
    publicKey: '',
    isValid: true,
  };

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
    this.getRoot();
  }

  getRoot(): void {
    this.layoutService.getRoot(this.user?.id.toString() || '').subscribe({
      next: (result) => {
        this.myCertificate = result;
        if (result) {
          this.showCreateButton = false;
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  onCreateRoot(): void {
    this.certificate.subjectUsername = this.user?.username || '';
    this.certificate.issuerUsername = this.user?.username || '';
    this.layoutService.createRoot(this.certificate).subscribe({
      next: (result) => {
        this.privKey = result.privateKey;
        this.pubKey = result.publicKey;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
