import { Component } from '@angular/core';
import { User } from 'src/app/model/user.model';
import { LayoutService } from '../layout.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { UserProfile } from 'src/app/model/user-profile.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChangePasswordComponent } from '../change-password/change-password.component';
import { Request } from 'src/app/model/request.model';
import { Commercial } from 'src/app/model/commercial.model';
import { CommercialRequestCreationComponent } from '../commercial-request-creation/commercial-request-creation.component';
import { CommercialCreationComponent } from '../commercial-creation/commercial-creation.component';
import { VPNMessage } from 'src/app/model/VPNMessage';

@Component({
  selector: 'app-profile-display',
  templateUrl: './profile-display.component.html',
  styleUrls: ['./profile-display.component.css'],
})
export class ProfileDisplayComponent implements OnInit {
  oldPassword: string = '';
  newPassword: string = '';
  repeatPassword: string = '';
  user: any;
  profile: UserProfile = {
    id: 0,
    username: '',
    password: '',
    name: '',
    surname: '',
    address: '',
    city: '',
    country: '',
    phone: '',
    isBlockedUser: false,
  };
  commercials: Commercial[] = [];
  requests: Request[] = [];
  ifCommercials: boolean = true;
  usernamesMap: { [key: number]: string } = {};
  message: VPNMessage={message:""};

  editProfileForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    name: new FormControl('', [Validators.required]),
    surname: new FormControl('', [Validators.required]),
    address: new FormControl('', [Validators.required]),
    city: new FormControl('', [Validators.required]),
    country: new FormControl('', [Validators.required]),
    phone: new FormControl('', [Validators.required]),
  });

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService,
    private router: Router,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
    if (this.user) {
      this.getUserProfile();
    }
    if (this.user?.role === 'employee') {
      this.getRequests();
      this.getCommercials();
    }
    if (this.user?.role === 'client') {
      this.getCommercials();
    }
    this.getMessage()
  }

  getUserProfile(): void {
    this.layoutService.getUserProfile(this.user.id!).subscribe(
      (profile: UserProfile) => {
        this.profile = profile;
      },
      (error) => {
        console.error('Error fetching user profile:', error);
      }
    );
  }

  update(): void {
    const editProfile: UserProfile = {
      id: this.profile.id,
      username: this.editProfileForm.value.username || '',
      password: this.profile.password || '',
      name: this.editProfileForm.value.name || '',
      surname: this.editProfileForm.value.surname || '',
      address: this.editProfileForm.value.address || '',
      city: this.editProfileForm.value.city || '',
      country: this.editProfileForm.value.country || '',
      phone: this.editProfileForm.value.phone || '',
      isBlockedUser: this.profile.isBlockedUser,
    };

    if (this.editProfileForm.valid) {
      this.layoutService.editUserProfile(editProfile).subscribe({
        next: (result: UserProfile) => {
          this.profile.id = result.id;
          this.profile.username = result.username;
          this.profile.password = result.password;
          this.profile.name = result.name;
          this.profile.surname = result.surname;
          this.profile.address = result.address;
          this.profile.city = result.city;
          this.profile.country = result.country;
          this.profile.phone = result.phone;
          this.profile.isBlockedUser = result.isBlockedUser;
          console.log('Profil nakon dodjele result: ');
          console.log(this.profile);
          this.router.navigate(['/profile']);
          this.snackBar.open('Succesfully edited profile', 'Close', {
            duration: 5000,
          });
        },
        error: () => {
          console.log(console.error());
        },
      });
    } else {
      this.snackBar.open('All fields must be entered correctly!', 'Close', {
        duration: 5000,
      });
    }
  }

  getCommercials(): void {
    this.layoutService.getCommercials().subscribe({
      next: (result: Commercial[]) => {
        this.commercials = result;
        if (this.user?.role === 'client') {
          console.log('usao');
          this.commercials = this.commercials.filter(
            (commercial) => commercial.client.id === this.user.id
          );
        }
        if (this.user?.role === 'employee') {
          this.commercials = this.commercials.filter(
            (commercial) => commercial.employee.id === this.user.id
          );
        }
      },
      error: () => {
        console.log(console.error());
      },
    });
  }

  getRequests(): void {
    this.layoutService.getRequests().subscribe({
      next: (result: Request[]) => {
        this.requests = result;
      },
      error: () => {
        console.log(console.error());
      },
    });
  }

  changePassword(): void {
    const dialogRef = this.dialog.open(ChangePasswordComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
    });
  }

  createCommercialRequest(): void {
    const dialogRef = this.dialog.open(CommercialRequestCreationComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
    });
  }

  createCommercial(request: Request): void {
    const dialogRef = this.dialog.open(CommercialCreationComponent, {
      width: '500px',
      data: request,
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
    });
  }

  viewRequests(): void {
    this.ifCommercials = false;
    this.getRequests();
  }

  viewCommercials(): void {
    this.ifCommercials = true;
    this.getCommercials();
  }
  getMessage():void{
    this.layoutService.getVPN().subscribe({
      next: (result: VPNMessage) => {
        this.message=result
      },
      error: () => {
        console.log(console.error());
      },
    });
  }
}
