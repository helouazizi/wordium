import { Component, Inject } from '@angular/core';
import {
  FormControl,
  Validators,
  ReactiveFormsModule,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
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

export interface ConfirmDialogResult {
  confirmed: boolean;
  reason?: string;
}

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
  readonly minLength: number;

  reasonControl = new FormControl('', {
    nonNullable: true,
  });

  constructor(
    private dialogRef: MatDialogRef<ConfirmDialog, ConfirmDialogResult>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData,
  ) {
    this.minLength = data.minReasonLength ?? 5;

    if (data.requireReason) {
      this.reasonControl.addValidators([
        Validators.required,
        Validators.minLength(this.minLength),
        this.noWhitespaceValidator,
      ]);
    }

    this.reasonControl.updateValueAndValidity({ emitEvent: false });
  }

  confirm(): void {
    this.reasonControl.markAsTouched();

    if (this.data.requireReason && this.reasonControl.invalid) {
      return;
    }

    const reason = this.reasonControl.value.trim();

    this.dialogRef.close({
      confirmed: true,
      reason,
    });
  }

  cancel(): void {
    this.dialogRef.close({ confirmed: false });
  }

  private noWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value as string;

    if (!value) return null;

    return value.trim().length === 0 ? { whitespace: true } : null;
  }
}
