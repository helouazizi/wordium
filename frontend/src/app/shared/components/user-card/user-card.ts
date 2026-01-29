import { Component, input, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserAvatar } from '../user-avatar/user-avatar';
import { User } from '../../models/user.model';

export type UserCardMode = 'dashboard' | 'search';

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
  mode = input<UserCardMode>('dashboard');

  onBan = output<number>();
  onUnban = output<number>();
  onDelete = output<number>();
  onFollow = output<number>();
  onViewProfile = output<number>();

  isConfirmingDelete = signal(false);

  confirmDelete() {
    this.isConfirmingDelete.set(true);
  }
  cancelDelete() {
    this.isConfirmingDelete.set(false);
  }

  executeDelete() {
    this.onDelete.emit(this.user().id);
    this.isConfirmingDelete.set(false);
  }
  get avatarText(): string {
    return this.user().username ? this.user().username[0].toUpperCase() : '?';
  }

  ban() {
    if (this.user()?.id) this.onBan.emit(this.user().id);
  }

  unban() {
    if (this.user()?.id) this.onUnban.emit(this.user().id);
  }



  follow() {
    if (this.user()?.id) this.onFollow.emit(this.user().id);
  }

  viewProfile() {
    if (this.user()?.id) this.onViewProfile.emit(this.user().id);
  }
}
