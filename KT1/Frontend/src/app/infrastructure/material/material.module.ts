import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatButton,
  MatButtonModule,
  MatIconButton,
} from '@angular/material/button';
import {
  MatFormField,
  MatFormFieldModule,
  MatLabel,
} from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTooltip } from '@angular/material/tooltip';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatDialogModule,
    MatTooltip
    ],
  exports: [
    MatButton,
    MatFormField,
    MatLabel,
    MatInput,
    MatIconButton,
    MatIcon,
    MatCardModule,
    MatFormFieldModule,
    MatButtonModule,
    MatDialogModule,
    MatTooltip
  ],
})
export class MaterialModule {}
