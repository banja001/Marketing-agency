import { Component } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { LayoutService } from '../layout.service';
import { AuthService } from '../../infrastructure/auth/auth.service';
import { Router } from '@angular/router';
import { Certificate } from '../../shared/model/certificate.model';

@Component({
  selector: 'app-certificate-view',
  templateUrl: './certificate-view.component.html',
  styleUrl: './certificate-view.component.css',
})
export class CertificateViewComponent {
  user: User | undefined;
  certificates: Certificate[] = [];
  displayedColumns: string[] = [];
  certificate: any;

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
    this.getCertificates();
    this.displayedColumns = [
      'subjectName',
      'issuerName',
      'startDate',
      'endDate',
      'type',
      'publicKey',
      'isValid',
      'getCert',
    ];
  }

  getCertificates(): void {
    this.layoutService.getAllCertificates().subscribe({
      next: (result) => {
        this.certificates = result;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  onRevoke(c: Certificate): void {
    this.layoutService.revokeCertificate(c).subscribe({
      next: (result) => {
        this.certificate = result;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
