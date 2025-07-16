import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { faXmark, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import {
  ClientType,
  Registration,
  ServicePackageType,
} from 'src/app/model/registration.model';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent {
  isPassword1Visible: boolean;
  isPassword2Visible: boolean;
  faXmark = faXmark;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  selectedClientType: string;

  isSuccessful = false;
  isUsing2FA = false;
  qrCodeImage: String = '';

  constructor(private authService: AuthService, private router: Router) {
    this.isPassword1Visible = false;
    this.isPassword2Visible = false;
    this.selectedClientType = ClientType.PHYSICAL_PERSON;
  }

  peRegistrationForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [this.passwordValidator()]),
    rePassword: new FormControl('', [this.passwordValidator()]),
    name: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    surname: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    address: new FormControl('', [Validators.required]),
    city: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    country: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    phoneNumber: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?:\\+381|0)[6-9][0-9]{6,8}$'),
    ]),
    servicePackage: new FormControl('', [Validators.required]),
    using2FA: new FormControl(false),
  });

  leRegistrationForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [this.passwordValidator()]),
    rePassword: new FormControl('', [this.passwordValidator()]),
    companyName: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    tin: new FormControl('', [
      Validators.required,
      Validators.pattern('^\\d{9}$'),
    ]),
    address: new FormControl('', [Validators.required]),
    city: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    country: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z][a-zA-Z ]+'),
    ]),
    phoneNumber: new FormControl('', [
      Validators.required,
      Validators.pattern('^(?:\\+381|0)[6-9][0-9]{6,8}$'),
    ]),
    servicePackage: new FormControl('', [Validators.required]),
    using2FA: new FormControl(false),
  });

  register(): void {
    if (
      this.selectedClientType === ClientType.PHYSICAL_PERSON &&
      this.peRegistrationForm.valid
    ) {
      if (
        this.peRegistrationForm.value.password !==
        this.peRegistrationForm.value.rePassword
      ) {
        alert(
          'Passwords do not match! Please make sure both passwords are identical'
        );
      } else {
        const registration: Registration = {
          id: NaN,
          name: this.peRegistrationForm.value.name || '',
          surname: this.peRegistrationForm.value.surname || '',
          username: this.peRegistrationForm.value.username || '',
          password: this.peRegistrationForm.value.password || '',
          address: this.peRegistrationForm.value.address || '',
          city: this.peRegistrationForm.value.city || '',
          country: this.peRegistrationForm.value.country || '',
          phone: this.peRegistrationForm.value.phoneNumber || '',
          enabled: false,
          servicePackage: this.mapToServicePackage(
            this.peRegistrationForm.value.servicePackage || ''
          ),
          clientType: this.selectedClientType,
          companyName: '',
          tin: '',
          using2FA: this.peRegistrationForm.value.using2FA || false,
        };
        console.log('USING 2FA PE: ' + registration.using2FA);
        this.authService.register(registration).subscribe({
          next: (result) => {
            //alert('Your registration request has been successfully added!');
            //this.router.navigate(['/login']);
            console.log('RES: ', result);
            this.isSuccessful = true;
            if (result) {
              this.isUsing2FA = true;
            } else {
              this.isUsing2FA = false;
            }
            this.qrCodeImage = result;
          },
          error: (error) => {
            console.error(error);
            if (error.status === 409) {
              alert('Username already exist!');
            }
          },
        });
      }
    } else if (
      this.selectedClientType === ClientType.LEGAL_ENTITY &&
      this.leRegistrationForm.valid
    ) {
      if (
        this.leRegistrationForm.value.password !==
        this.leRegistrationForm.value.rePassword
      ) {
        console.log(
          'Passwords do not match! Please make sure both passwords are identical'
        );
      } else {
        const registration: Registration = {
          id: NaN,
          companyName: this.leRegistrationForm.value.companyName || '',
          tin: this.leRegistrationForm.value.tin || '',
          username: this.leRegistrationForm.value.username || '',
          password: this.leRegistrationForm.value.password || '',
          address: this.leRegistrationForm.value.address || '',
          city: this.leRegistrationForm.value.city || '',
          country: this.leRegistrationForm.value.country || '',
          phone: this.leRegistrationForm.value.phoneNumber || '',
          enabled: false,
          servicePackage: this.mapToServicePackage(
            this.leRegistrationForm.value.servicePackage || ''
          ),
          clientType: this.selectedClientType,
          name: '',
          surname: '',
          using2FA: this.leRegistrationForm.value.using2FA || false,
        };
        console.log('USING 2FA LE: ' + registration.using2FA);
        this.authService.register(registration).subscribe({
          next: (result) => {
            //alert('Your registration request has been successfully added!');
            //this.router.navigate(['/login']);
            this.isSuccessful = true;
            if (result) {
              this.isUsing2FA = true;
            } else {
              this.isUsing2FA = false;
            }
            this.qrCodeImage = result;
          },
          error: (error) => {
            console.error(error);
            if (error.status === 409) {
              alert('Username already exist!');
            }
          },
        });
      }
    }
  }

  togglePassword1Visibility() {
    this.isPassword1Visible = !this.isPassword1Visible;
  }

  togglePassword2Visibility() {
    this.isPassword2Visible = !this.isPassword2Visible;
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

  mapToServicePackage(value: string): ServicePackageType {
    switch (value) {
      case 'BASIC':
        return ServicePackageType.BASIC;
      case 'STANDARD':
        return ServicePackageType.STANDARD;
      case 'GOLD':
        return ServicePackageType.GOLD;
      default:
        return ServicePackageType.BASIC;
    }
  }
}
