import { Component } from '@angular/core';
import { User, UserModel } from '../../infrastructure/auth/model/user.model';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { LayoutService } from '../layout.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { KeyPair } from '../../shared/model/keypair.model';
import {
  Certificate,
  CreateCertificate,
} from '../../shared/model/certificate.model';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrl: './create-certificate.component.css',
})
export class CreateCertificateComponent {
  user: any;
  issuerUsername: string = '';
  pubKey: string = '';
  privKey: string = '';
  hasCert: boolean = false;
  subject: UserModel = {
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
  };
  issuer: UserModel = {
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
  };

  keysForm = new FormGroup({
    pubKey: new FormControl('', [Validators.required]),
    privKey: new FormControl('', [Validators.required]),
  });

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.issuerUsername = params['username'];
      this.authService.user$.subscribe((user) => {
        this.user = user;
        this.getIssuer();
      });
    });
  }

  getIssuer(): void {
    this.authService.getUserByUsername(this.issuerUsername).subscribe({
      next: (result) => {
        this.issuer = result;
        this.getSubject();
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  getSubject(): void {
    this.authService.getUserByUsername(this.user.username).subscribe({
      next: (result) => {
        this.subject = result;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  onCreate(): void {
    const cert: CreateCertificate = {
      subjectUsername: this.user?.username || '',
      issuerUsername: this.issuerUsername || '',
      privateKey: this.keysForm.value.privKey || '',
      publicKey: this.keysForm.value.pubKey || '',
    };

    // validacija ako jedno unese a drugo ne
    this.layoutService.createCert(cert).subscribe({
      next: (result) => {
        this.privKey = result.privateKey;
        this.pubKey = result.publicKey;
        this.hasCert = true;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
