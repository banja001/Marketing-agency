import { Component } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { LayoutService } from '../layout.service';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { Router } from '@angular/router';
import { Certificate } from '../../shared/model/certificate.model';
import { faL } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-im-certificate',
  templateUrl: './im-certificate.component.html',
  styleUrl: './im-certificate.component.css',
})
export class ImCertificateComponent {
  user: User | undefined;
  certificates: Certificate[] = [];
  displayedColumns: string[] = [];
  isValid: boolean = true;

  hasCert: Boolean = false;

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
    this.getImIssuers();
    this.displayedColumns = [
      'subjectName',
      'issuerName',
      'startDate',
      'endDate',
      'type',
      'publicKey',
      'getCert',
    ];
    this.getCert();
  }

  getImIssuers(): void {
    this.layoutService.getImIssuers().subscribe({
      next: (result) => {
        this.certificates = result;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  onIssue(username: string): void {
    this.router.navigate(['/createCert/' + username]);
  }

  getCert(): void {
    this.layoutService.getRoot(this.user?.id.toString() || '').subscribe({
      next: (result) => {
        console.log(result);
        if (result) {
          this.myCertificate = result;
          this.hasCert = true;
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
