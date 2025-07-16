import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ProfileDisplayComponent } from './profile-display/profile-display.component';
import { MaterialModule } from '../infrastructure/material/material.module';
import { FormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { MatDialogModule } from '@angular/material/dialog';
import { UsersDisplayComponent } from './staff-display/users-display.component';
import { UserCreationComponent } from './user-creation/user-creation.component';
import { MatSelectModule } from '@angular/material/select';
import { CommercialRequestCreationComponent } from './commercial-request-creation/commercial-request-creation.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { CommercialCreationComponent } from './commercial-creation/commercial-creation.component';

@NgModule({
  declarations: [
    ProfileDisplayComponent,
    ChangePasswordComponent,
    UsersDisplayComponent,
    UserCreationComponent,
    CommercialRequestCreationComponent,
    CommercialCreationComponent,
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MaterialModule,
    FormsModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
})
export class LayoutModule {}
