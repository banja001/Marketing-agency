import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user.model';
import { AuthService } from '../infrastructure/auth/auth.service';

@Component({
  selector: 'xp-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  isDropdownOpen = false;
  systemAdminEnabled = false;
  user: User | undefined;

  constructor(public router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  permissions(): void {
    this.router.navigate(['/permissions']);
  }

  logout(): void {
    this.authService.logout();
  }

  register(): void {
    this.router.navigate(['/register']);
  }

  registrationRequests(): void {
    this.router.navigate(['/registrationRequests']);
  }

  profile(): void {
    this.router.navigate(['/profile']);
  }

  staff(): void {
    this.router.navigate(['/staff']);
  }

  create(): void {
    this.router.navigate(['/createStaffMember']);
  }

  notifications(): void {
    this.router.navigate(['/notifications']);
  }

  requestDataDeletion(): void {
    if (
      confirm(
        'Are you sure you want to delete your account? This action is irreversible and all your data will be permanently deleted.'
      ) &&
      this.user &&
      this.user.username
    ) {
      console.log(this.user);
      this.authService.deleteAccount(this.user?.username).subscribe({
        next: () => {
          alert('Your account is deleted successfully.');
          this.logout();
        },
        error: (error) => {
          console.error('Error deleting account:', error);
          if (error.status === 400) {
            alert('Sorry, you cannot delete your account.');
          } else if (error.status === 401) {
            alert('Authentication error. Please log in again.');
            this.router.navigate(['/login']);
          } else if (error.status === 409) {
            alert(
              'A conflict occurred while deleting the account. Please try again later.'
            );
          } else {
            alert(
              'An error occurred while deleting the account. Please try again later.'
            );
          }
        },
      });
    }
  }
}
