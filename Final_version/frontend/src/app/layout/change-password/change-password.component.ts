import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { LayoutService } from '../layout.service';
import { User } from 'src/app/model/user.model';
import { AuthService } from 'src/app/infrastructure/auth/auth.service';
import { ChangePassword } from 'src/app/model/change-password.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent {
  oldPassword: string = '';
  newPassword: string = '';
  repeatPassword: string = '';
  user: User | undefined;
  passwordToChange: ChangePassword | undefined;

  constructor(
    private authService: AuthService,
    public dialogRef: MatDialogRef<ChangePasswordComponent>,
    private layoutService: LayoutService,
    @Inject(MAT_DIALOG_DATA) public isInitialLogging: boolean
  ) {
    if (isInitialLogging) dialogRef.disableClose = true;
  }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
    });
    console.log('INITIAL LOGGING');
    console.log(this.isInitialLogging);
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  isFormValid(): boolean {
    return (
      this.oldPassword != '' &&
      this.newPassword != '' &&
      this.repeatPassword != ''
    );
  }

  changePassword(): void {
    this.passwordToChange = {
      username: this.user?.username!,
      oldPassword: this.oldPassword,
      newPassword: this.newPassword,
      repeatedPassword: this.repeatPassword,
    };

    this.layoutService.changePassword(this.passwordToChange).subscribe({
      next: (result: any) => {
        this.closeDialog();
      },
      error: () => {
        console.log(console.error());
      },
    });
  }
}
