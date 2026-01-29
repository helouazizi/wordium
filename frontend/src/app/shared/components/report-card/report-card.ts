import { Component, input, output, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCard, MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserAvatar } from '../user-avatar/user-avatar';
import { Report } from '../../../core/apis/posts/post.model';

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

  isConfirmingDelete = signal(false);

  confirmDelete() {
    this.isConfirmingDelete.set(true);
  }
  cancelDelete() {
    this.isConfirmingDelete.set(false);
  }

  executeDelete() {
    this.onDelete.emit(this.report().id);
    this.isConfirmingDelete.set(false);
  }

  get isPostReport() {
    return this.report().type === 'post';
  }

  get isUserReport() {
    return this.report().type === 'user';
  }
}
