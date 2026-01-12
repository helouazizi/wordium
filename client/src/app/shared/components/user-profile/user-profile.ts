import { Component, Input } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { User } from '../../../shared/models/user';
import { TimeAgo } from '../../pipes/time-ago.pipe';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, TimeAgo],

  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.scss'],
})
export class UserProfile {
  @Input() user: User = {} as User; 
  @Input() time: string = '';

  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() showEmail = false;
  @Input() showName = false;
  @Input() showTime = false;

  get initial(): string {
    return this.user?.username?.charAt(0)?.toUpperCase() || '?';
  }

  get hasAvatar(): boolean {
    return !!this.user?.avatar;
  }
}
