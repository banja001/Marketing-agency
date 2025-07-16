import { Component, OnInit } from '@angular/core';
import { User } from '../../infrastructure/auth/model/user.model';
import { Router } from '@angular/router';
import { AuthService } from '../../infrastructure/auth/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
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

  goToCertificates(): void {
    this.router.navigate(['/certView']);
  }

  goToMyRoot(): void {
    this.router.navigate(['/myRoot']);
  }

  goToCertificate(): void {
    this.router.navigate(['/myImCert']);
  }

  logout(): void {
    this.authService.logout();
  }

  search(): void {
    this.router.navigate(['/search']);
  }
}
