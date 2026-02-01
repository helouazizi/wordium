import { Component, Inject } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

export interface ConfirmDialogData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  color?: 'primary' | 'warn';
  requireReason?: boolean;
  reasonLabel?: string;
  minReasonLength?: number;
}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.scss',
})
export class ConfirmDialog {
  reasonControl = new FormControl('');

  constructor(
    private dialogRef: MatDialogRef<ConfirmDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData,
  ) {
    if (data.requireReason) {
      this.reasonControl.setValidators([
        Validators.required,
        Validators.minLength(data.minReasonLength || 5),
      ]);

      this.reasonControl.updateValueAndValidity({ emitEvent: false });
    }
  }

  confirm() {
    if (this.data.requireReason && this.reasonControl.invalid) return;

    this.dialogRef.close({
      confirmed: true,
      reason: this.reasonControl.value,
    });
  }

  cancel() {
    this.dialogRef.close({ confirmed: false });
  }
}
