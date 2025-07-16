import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModule } from '../material/material.module';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { PasswordlessLoginComponent } from './passwordless-login/passwordless-login.component';
import { PasswordlessVerificationComponent } from './passwordless-verification/passwordless-verification.component';
import { AccountActivationComponent } from './account-activation/account-activation.component';
import { RecaptchaFormsModule, RecaptchaModule } from 'ng-recaptcha';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { NewPasswordComponent } from './new-password/new-password.component';
import { TotpComponent } from './totp/totp.component';

@NgModule({
  declarations: [
    LoginComponent,
    PasswordlessLoginComponent,
    PasswordlessVerificationComponent,
    RegistrationComponent,
    AccountActivationComponent,
    ForgotPasswordComponent,
    NewPasswordComponent,
    TotpComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    HttpClientModule,
    MaterialModule,
    FormsModule,
    RecaptchaModule,
    RecaptchaFormsModule,
  ],
  exports: [],
})
export class AuthModule {}
