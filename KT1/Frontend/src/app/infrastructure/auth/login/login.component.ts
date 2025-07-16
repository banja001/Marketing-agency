import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { LoginCredentials } from '../model/login-credentials.model';
import { Router } from '@angular/router';
import { User } from '@auth0/auth0-angular';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit{
  isPasswordVisible= false;
  faEye = faEye;
  faEyeSlash = faEyeSlash;

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.isPasswordVisible = false;
  }

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

  login(): void {
    const login: LoginCredentials = {
      username: this.loginForm.value.username || '',
      password: this.loginForm.value.password || '',
    };
    if (this.loginForm.valid) {
      this.authService.login(login).subscribe({
        next: () => {
          alert('Succesfull log in!');
          this.router.navigate(['']);
        },
        error: (error) => {
          alert(error);
        },
      });
    }
  }
  logout(): void {

      this.authService.logout()
    
  }
}
