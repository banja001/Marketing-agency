import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { RecoverAccount } from 'src/app/model/change-password.model';
import { faEye, faEyeSlash, faXmark } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css'],
})
export class NewPasswordComponent {
  isPassword1Visible: boolean;
  isPassword2Visible: boolean;
  user: any;
  userProfile: any;
  token: string = '';
  error: Boolean = true;
  faXmark = faXmark;
  faEye = faEye;
  faEyeSlash = faEyeSlash;

  passwordForm = new FormGroup({
    newPassword: new FormControl('', [this.passwordValidator()]),
    rePassword: new FormControl('', [this.passwordValidator()]),
  });

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.isPassword1Visible = false;
    this.isPassword2Visible = false;
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.token = params['id'];
      this.token = this.token.split('=')[1];
      console.log('From route: ', this.token);
    });
  }

  recoverAccount(): void {
    if (this.passwordForm.valid && this.token !== '') {
      {
        if (
          this.passwordForm.value.newPassword !==
          this.passwordForm.value.rePassword
        ) {
          alert(
            'Passwords do not match! Please make sure both passwords are identical'
          );
        } else {
          const credentials: RecoverAccount = {
            token: this.token,
            newPassword: this.passwordForm.value.newPassword!,
          };
          console.log('Credentials Token: ', credentials.token);
          console.log('This.token: ', this.token);
          this.authService.recoverAccount(credentials).subscribe({
            next: (result) => {
              if (result) {
                this.error = false;
                console.log(result);
              } else {
                this.error = false;
              }
              setTimeout(() => {
                this.router.navigate(['/login']);
              }, 4000);
            },
            error: (error) => {
              this.error = true;
              alert(error);
              setTimeout(() => {
                this.router.navigate(['/login']);
              }, 4000);
            },
          });
        }
      }
    }
  }

  passwordValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) {
        return { required: true };
      }

      const lowerCase = value.match(/[a-z]/) !== null;
      const upperCase = value.match(/[A-Z]/) !== null;
      const numbers = value.match(/[0-9]/) !== null;
      const specialCharacters =
        value.match(/[!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~]/) !== null;
      const minLength = value.length >= 8;

      const valid =
        lowerCase && upperCase && numbers && specialCharacters && minLength;
      return !valid ? { passwordStrength: true } : null;
    };
  }

  togglePassword1Visibility() {
    this.isPassword1Visible = !this.isPassword1Visible;
  }

  togglePassword2Visibility() {
    this.isPassword2Visible = !this.isPassword2Visible;
  }
}
