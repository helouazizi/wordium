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
import { UserListSource } from '../user-list/user-list';

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

  isConfirmingDelete = signal(false);
  isConfirmingBan = signal(false);
  isConfirmingUnBan = signal(false);
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

  confirmBan() {
    this.isConfirmingBan.set(true);
  }
  cancelBan() {
    this.isConfirmingBan.set(false);
  }

   confirmUnBan() {
    this.isConfirmingUnBan.set(true);
  }
  cancelUnBan() {
    this.isConfirmingUnBan.set(false);
  }

  executeBan() {
    if (this.user()?.id) this.onBan.emit(this.user().id);
    this.isConfirmingBan.set(false);
  }
  executeUnBan() {
    if (this.user()?.id) this.onUnban.emit(this.user().id);
    this.isConfirmingUnBan.set(false);
  }

  get avatarText(): string {
    return this.user().username ? this.user().username[0].toUpperCase() : '?';
  }



  follow() {
    if (this.user()?.id) this.onFollow.emit(this.user().id);
  }

  viewProfile() {
    if (this.user()?.id) this.onViewProfile.emit(this.user().id);
  }
}
