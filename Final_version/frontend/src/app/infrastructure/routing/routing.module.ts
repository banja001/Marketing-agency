import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../auth/login/login.component';
import { RegistrationComponent } from '../auth/registration/registration.component';
import { PasswordlessLoginComponent } from '../auth/passwordless-login/passwordless-login.component';
import { PermissionsComponent } from '../../layout/permissions/permissions.component';
import { PasswordlessVerificationComponent } from '../auth/passwordless-verification/passwordless-verification.component';
import { RegistrationRequestsComponent } from 'src/app/admin/registration-requests/registration-requests.component';
import { ProfileDisplayComponent } from 'src/app/layout/profile-display/profile-display.component';
import { UsersDisplayComponent } from 'src/app/layout/staff-display/users-display.component';
import { UserCreationComponent } from 'src/app/layout/user-creation/user-creation.component';
import { AccountActivationComponent } from '../auth/account-activation/account-activation.component';
import { authGuard } from '../auth/guards/auth.guard';
import { ForgotPasswordComponent } from '../auth/forgot-password/forgot-password.component';
import { NewPasswordComponent } from '../auth/new-password/new-password.component';
import { NotificationComponent } from 'src/app/admin/notification/notification.component';
import { TotpComponent } from '../auth/totp/totp.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'totp/:username', component: TotpComponent },
  { path: 'passwordlessLogin', component: PasswordlessLoginComponent },
  { path: 'permissions', component: PermissionsComponent },
  {
    path: 'passwordlessLogin/verify/:id',
    component: PasswordlessVerificationComponent,
  },
  { path: 'register', component: RegistrationComponent },
  {
    path: 'registrationRequests',
    component: RegistrationRequestsComponent,
    canActivate: [authGuard('admin')],
  },
  { path: 'profile', component: ProfileDisplayComponent },
  {
    path: 'staff',
    component: UsersDisplayComponent,
    canActivate: [authGuard('admin')],
  },
  {
    path: 'createStaffMember',
    component: UserCreationComponent,
    canActivate: [authGuard('admin')],
  },
  {
    path: 'accountActivation/:id',
    component: AccountActivationComponent,
    canActivate: [authGuard('client')],
  },
  { path: 'forgotPassword', component: ForgotPasswordComponent },
  { path: 'newPassword/:id', component: NewPasswordComponent },
  { path: 'notifications', component: NotificationComponent },
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class RoutingModule {}
