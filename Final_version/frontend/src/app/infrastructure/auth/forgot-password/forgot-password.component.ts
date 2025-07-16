import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Passwordless } from 'src/app/model/login.model';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css'],
})
export class ForgotPasswordComponent {
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
  }

  sendMail(): void {
    if (this.loginForm.valid && this.token !== undefined) {
      const mail: Passwordless = {
        email: this.loginForm.value.email || '',
        recaptchaToken: this.token,
      };

      this.authService.sendForgottenPasswordMail(mail).subscribe({
        next: (result) => {
          if (result) {
            alert('We sent you mail for changing password!');
            this.router.navigate(['']);
          } else {
            this.router.navigate(['login']);
          }
        },
        error: () => {
          alert('Cannot send mail to this user.');
        },
      });
    }
  }
}
