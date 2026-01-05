import { Component, Input } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { User } from '../../../shared/models/user';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule],

  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.scss'],
})
export class UserProfile {
  @Input({ required: true }) user!: User;
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() showEmail = false;
   @Input() showName = false;

  get initial(): string {
    return this.user?.username?.charAt(0)?.toUpperCase() || '?';
  }

  get hasAvatar(): boolean {
    return !!this.user?.avatar;
  }
}
