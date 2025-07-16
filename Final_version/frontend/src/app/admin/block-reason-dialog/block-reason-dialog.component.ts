import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
@Component({
  selector: 'app-block-reason-dialog',
  templateUrl: './block-reason-dialog.component.html',
  styleUrls: ['./block-reason-dialog.component.css']
})

export class BlockReasonDialogComponent {
  reason: string = '';
  constructor(public dialogRef: MatDialogRef<BlockReasonDialogComponent>) {}
 
  close(): void {
    this.dialogRef.close();
  }
}
