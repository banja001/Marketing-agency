import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Registration } from 'src/app/model/registration.model';
import { BlockReasonDialogComponent } from '../block-reason-dialog/block-reason-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';

@Component({
  selector: 'app-registration-requests',
  templateUrl: './registration-requests.component.html',
  styleUrls: ['./registration-requests.component.css'],
})
export class RegistrationRequestsComponent implements OnInit {
  requests: Registration[] = [];

  constructor(
    public router: Router,
    private service: AdminService,
    private dialog: MatDialog,
    private authService: AuthService,
    private tokenService: TokenStorage
  ) {}

  ngOnInit(): void {
    this.getRequests();
  }

  getRequests(): void {
    this.service.getRequests().subscribe({
      next: (result: any) => {
        this.requests = result;
        console.log(result);
      },
      error: () => {
        console.log(console.error());
      },
    });
  }

  accept(request: Registration): void {
    this.service.sendMail(request.username).subscribe({
      next: (result: any) => {
        //window.location.reload();
      },
      error: (error) => {
        console.log(error);
      },
    });
    // this.authService
    //   .refreshToken(this.tokenService.getRefreshToken() || '')
    //   .subscribe({
    //     next: (result) => {
    //       console.log('GOTOVO');
    //     },
    //     error: (error) => {
    //       console.log(error);
    //     },
    //   });
  }

  block(request: Registration): void {
    const dialogRef = this.dialog.open(BlockReasonDialogComponent, {
      width: '250px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.service.blockRequest(request.username, result).subscribe({
          next: (result: any) => {
            window.location.reload();
          },
          error: (error) => {
            console.log(error);
          },
        });
      }
    });
  }
}
