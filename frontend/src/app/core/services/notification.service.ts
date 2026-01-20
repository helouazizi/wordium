import { inject, Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private snackBar = inject(MatSnackBar);

  private readonly config: MatSnackBarConfig = {
    duration: 5000,
    horizontalPosition: 'end',
    verticalPosition: 'top',
  };

  showError(message: string) {
    this.snackBar.open(message, 'Close', {
      ...this.config,
      panelClass: ['error-snackbar'],

    });
  }

  showSuccess(message: string) {
    this.snackBar.open(message, 'OK', {
      ...this.config,
      panelClass: ['success-snackbar'],
    });
  }
}
