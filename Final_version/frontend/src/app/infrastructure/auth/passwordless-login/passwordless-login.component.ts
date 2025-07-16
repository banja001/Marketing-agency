import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Passwordless } from 'src/app/model/login.model';

@Component({
  selector: 'app-passwordless-login',
  templateUrl: './passwordless-login.component.html',
  styleUrls: ['./passwordless-login.component.css'],
})
export class PasswordlessLoginComponent {
  token: string | undefined;

  constructor(private authService: AuthService, private router: Router) {
    this.token = undefined;
  }

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  onCaptchaResolved(captchaResponse: any) {
    console.log(`Resolved captcha with response: ${captchaResponse}`);
    this.token = captchaResponse;
    // You can now send this token along with your form data to the server
  }

  login(): void {
    if (this.loginForm.valid && this.token !== undefined) {
      const login: Passwordless = {
        email: this.loginForm.value.email || '',
        recaptchaToken: this.token,
      };
      this.authService.sendVerificationEmail(login).subscribe({
        next: (result) => {
          if (result) {
            alert('We sent you verification mail!');
            this.router.navigate(['']);
          } else {
            alert(
              "You cannot do passwordless login, since you don't have Standard or Gold package"
            );
            this.router.navigate(['login']);
          }
        },
        error: () => {
          alert('User not found');
        },
      });
    }
  }
}
