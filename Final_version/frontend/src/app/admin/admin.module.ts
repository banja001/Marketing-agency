import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistrationRequestsComponent } from './registration-requests/registration-requests.component';
import { MaterialModule } from '../infrastructure/material/material.module';
import { BlockReasonDialogComponent } from './block-reason-dialog/block-reason-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { NotificationComponent } from './notification/notification.component';


@NgModule({
  declarations: [RegistrationRequestsComponent, BlockReasonDialogComponent, NotificationComponent],
  imports: [
    CommonModule,
    MaterialModule,
    MatDialogModule,
    FormsModule
  ]
})
export class AdminModule { }
