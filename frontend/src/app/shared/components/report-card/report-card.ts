import { Component, input, output, computed, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCard, MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserAvatar } from '../user-avatar/user-avatar';
import { Report } from '../../../core/apis/posts/post.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-report-card',
  standalone: true,
  templateUrl: './report-card.html',
  styleUrl: './report-card.scss',
  imports: [UserAvatar, MatIcon, MatCard],
})
export class ReportCard {
  report = input.required<Report>();
  isHandset = input<boolean>();

  onResolve = output<number>();
  onDelete = output<number>();
  onViewTarget = output<Report>();

  private dialog = inject(MatDialog);

  get isPostReport() {
    return this.report().type === 'post';
  }

  get isUserReport() {
    return this.report().type === 'user';
  }

  confirmDeleteReport() {
    this.dialog.open(ConfirmDialog, {
      width: '420px',
      disableClose: true,
      data: {
        title: 'Delete Report',
        message: 'This report will be permanently deleted.',
        confirmText: 'Delete',
        color: 'warn',
      },
    }).afterClosed().subscribe(res => {
      if (res?.confirmed) this.onDelete.emit(this.report().id);
    });
  }

  confirmResolveReport() {
    this.dialog.open(ConfirmDialog, {
      width: '420px',
      disableClose: true,
      data: {
        title: 'Resolve Report',
        message: 'Mark this report as resolved?',
        confirmText: 'Resolve',
        color: 'primary',
      },
    }).afterClosed().subscribe(res => {
      if (res?.confirmed) this.onResolve.emit(this.report().id);
    });
  }
}
