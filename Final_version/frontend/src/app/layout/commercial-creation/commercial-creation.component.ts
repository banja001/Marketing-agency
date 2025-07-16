import { Component, Inject } from '@angular/core';
import { LayoutService } from '../layout.service';
import { Router } from '@angular/router';
import { DialogRef } from '@angular/cdk/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { Commercial } from 'src/app/model/commercial.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Request } from 'src/app/model/request.model';
import { Employee } from 'src/app/model/employee.model';
import { UserProfile } from 'src/app/model/user-profile.model';

@Component({
  selector: 'app-commercial-creation',
  templateUrl: './commercial-creation.component.html',
  styleUrls: ['./commercial-creation.component.css'],
})
export class CommercialCreationComponent {
  user: any;
  userProfile: any;

  constructor(
    private layoutService: LayoutService,
    private router: Router,
    private authService: AuthService,
    private dialogRef: DialogRef,
    @Inject(MAT_DIALOG_DATA) public data: Request
  ) {}

  commercialForm = new FormGroup({
    moto: new FormControl('', [Validators.required]),
    duration: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
  });

  ngOnInit() {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });

    this.getUserProfile();
  }

  createCommercial(): void {
    const employee: Employee = {
      id: this.user.id,
      username: this.userProfile.username,
      password: this.userProfile.password,
      name: this.userProfile.name,
      surname: this.userProfile.surname,
      address: this.userProfile.address,
      city: this.userProfile.city,
      country: this.userProfile.country,
      phone: this.userProfile.phone,
      isBlockedUser: this.userProfile.isBlockedUser,
      enabled: true,
      hasChangedPassword: true,
    };

    const commercial: Commercial = {
      employee: employee,
      client: this.data.client || '',
      moto: this.commercialForm.value.moto || '',
      duration: this.commercialForm.value.duration || '',
      description: this.commercialForm.value.description || '',
    };

    if (this.commercialForm.valid) {
      this.layoutService.createCommercial(commercial).subscribe({
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
        complete: () => {
          this.layoutService.deleteRequest(this.data.id).subscribe({
            next: () => {},
            error: () => {
              console.error();
            },
          });
        },
      });
    }
  }

  getUserProfile(): void {
    this.layoutService.getUserProfile(this.user?.id!).subscribe(
      (profile: UserProfile) => {
        this.userProfile = profile;
      },
      (error) => {
        console.error('Error fetching user profile:', error);
      }
    );
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
