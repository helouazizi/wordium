import { Component, inject, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserAvatar } from '../user-avatar/user-avatar';
import { User } from '../../models/user.model';
import { UserListSource } from '../user-list/user-list';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatMenuModule,
    MatTooltipModule,
    UserAvatar,
  ],
  templateUrl: './user-card.html',
  styleUrl: './user-card.scss',
})
export class UserCard {
  user = input.required<User>();
  mode = input<UserListSource>('dashboard');
  isOwner = input<boolean>();

  onBan = output<number>();
  onUnban = output<number>();
  onDelete = output<number>();
  onFollow = output<number>();
  onViewProfile = output<number>();

  private dialog = inject(MatDialog);

  get avatarText(): string {
    return this.user().username ? this.user().username[0].toUpperCase() : '?';
  }

  confirmDeleteUser() {
    this.dialog
      .open(ConfirmDialog, {
        width: '420px',
        disableClose: true,
        data: {
          title: 'Delete User',
          message: 'This user account will be permanently deleted.',
          confirmText: 'Delete',
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) this.onDelete.emit(this.user().id);
      });
  }

  confirmBanUser() {
    this.dialog
      .open(ConfirmDialog, {
        width: '450px',
        disableClose: true,
        data: {
          title: 'Ban User',
          message: 'This user account will be permanently banned.',
          confirmText: 'Ban User',
          color: 'warn',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) this.onBan.emit(this.user().id);
      });
  }

  confirmUnbanUser() {
    this.dialog
      .open(ConfirmDialog, {
        width: '420px',
        disableClose: true,
        data: {
          title: 'Unban User',
          message: 'This user will regain access.',
          confirmText: 'Unban',
          color: 'primary',
        },
      })
      .afterClosed()
      .subscribe((res) => {
        if (res?.confirmed) this.onUnban.emit(this.user().id);
      });
  }

  viewProfile() {
    this.onViewProfile.emit(this.user().id);
  }

  toggleFollow() {
    this.onFollow.emit(this.user().id);
  }
}
