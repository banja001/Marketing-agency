import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { Login } from 'src/app/model/login.model';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user.model';
import { LayoutService } from 'src/app/layout/layout.service';
import { Employee } from 'src/app/model/employee.model';
import { MatDialog } from '@angular/material/dialog';
import { ChangePasswordComponent } from 'src/app/layout/change-password/change-password.component';
import { TokenStorage } from '../jwt/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  isPasswordVisible: boolean;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  user: User | undefined;
  isInitialLogging: boolean = true;
  token: string | undefined;

  constructor(
    private authService: AuthService,
    private layoutService: LayoutService,
    private dialog: MatDialog,
    private router: Router,
    private tokenStorage: TokenStorage
  ) {
    this.isPasswordVisible = false;
    this.token = undefined;
  }

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

  onCaptchaResolved(captchaResponse: any) {
    console.log(`Resolved captcha with response: ${captchaResponse}`);
    this.token = captchaResponse;
    // You can now send this token along with your form data to the server
  }

  login(): void {
    if (this.loginForm.valid && this.token !== undefined) {
      const login: Login = {
        username: this.loginForm.value.email || '',
        password: this.loginForm.value.password || '',
        recaptchaToken: this.token,
      };

      this.authService.login(login).subscribe({
        next: () => {
          console.log('TOKEN: ', this.tokenStorage.getAccessToken());
          if (this.tokenStorage.getAccessToken() === null) {
            this.router.navigate(['/totp', login.username]);
          } else {
            alert('Succesfully logged in!');

            this.authService.user$.subscribe((user) => {
              this.user = user;
            });

            if (this.user?.role === 'employee')
              this.layoutService.getEmployee(this.user.id).subscribe({
                next: (result: Employee) => {
                  if (!result.hasChangedPassword) {
                    const dialogRef = this.dialog.open(
                      ChangePasswordComponent,
                      {
                        width: '400px',
                        data: this.isInitialLogging,
                      }
                    );
                  }
                },
                error: (error) => {
                  console.error('Error:', error);
                },
              });

            this.router.navigate(['']);
          }
        },
        error: (error) => {
          alert('Sorry, this user is unavailable.');
        },
      });
    }
  }
}
