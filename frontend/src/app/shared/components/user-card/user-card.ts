import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserAvatar } from '../user-avatar/user-avatar';

export type UserCardMode = 'dashboard' | 'search';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [
    CommonModule, MatCardModule, MatButtonModule, 
    MatIconModule, MatChipsModule, MatMenuModule, MatTooltipModule,UserAvatar
  ],
  templateUrl: './user-card.html',
  styleUrl: './user-card.scss'
})
export class UserCard {
  // New Angular Signal Inputs
  user = input.required<any>(); 
  mode = input<UserCardMode>('dashboard');

  // Outputs for parent interaction
  onBan = output<any>();
  onUnban = output<any>();
  onDelete = output<any>();
  onFollow = output<any>();
  onViewProfile = output<any>();

  get avatarText(): string {
    return this.user().name ? this.user().name[0].toUpperCase() : '?';
  }
}