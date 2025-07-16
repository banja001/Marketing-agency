import { Component } from '@angular/core';
import { LayoutService } from '../layout.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RequestCreation } from 'src/app/model/request-creation.model';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { DialogRef } from '@angular/cdk/dialog';
import { UserProfile } from 'src/app/model/user-profile.model';

@Component({
  selector: 'app-commercial-request-creation',
  templateUrl: './commercial-request-creation.component.html',
  styleUrls: ['./commercial-request-creation.component.css'],
})
export class CommercialRequestCreationComponent {
  minDate = new Date();
  user: any;

  constructor(
    private layoutService: LayoutService,
    private router: Router,
    private authService: AuthService,
    private dialogRef: DialogRef
  ) {}

  requestForm = new FormGroup({
    deadline: new FormControl('', [Validators.required]),
    activeFrom: new FormControl('', [Validators.required]),
    activeTo: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
  });

  ngOnInit() {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
  }

  createRequest(): void {
    const request: RequestCreation = {
      clientId: this.user.id,
      deadline: this.requestForm.value.deadline || '',
      activeFrom: this.requestForm.value.activeFrom || '',
      activeTo: this.requestForm.value.activeTo || '',
      description: this.requestForm.value.description || '',
    };

    let date = new Date(request.activeFrom);
    let day = String(date.getDate()).padStart(2, '0');
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let year = date.getFullYear();
    let formattedDateFrom = `${day}-${month}-${year}`;

    date = new Date(request.activeTo);
    day = String(date.getDate()).padStart(2, '0');
    month = String(date.getMonth() + 1).padStart(2, '0');
    year = date.getFullYear();
    let formattedDateTo = `${day}-${month}-${year}`;

    date = new Date(request.deadline);
    day = String(date.getDate()).padStart(2, '0');
    month = String(date.getMonth() + 1).padStart(2, '0');
    year = date.getFullYear();
    let formattedDateDeadline = `${day}-${month}-${year}`;

    request.deadline = formattedDateDeadline;
    request.activeFrom = formattedDateFrom;
    request.activeTo = formattedDateTo;

    if (this.requestForm.valid) {
      this.layoutService.createRequest(request).subscribe({
        next: () => {
          alert('Successfully created!');
          this.router.navigate(['/profile']);
          this.closeDialog();
        },
        error: (err) => {
          if (err.status === 403) {
            alert('Forbidden');
          } else {
            console.error('Error:', err);
          }
        },
      });
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
