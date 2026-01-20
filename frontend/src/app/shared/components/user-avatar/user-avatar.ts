import { Component, input, output, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { User } from '../../models/user.model';

export type AvatarSize = 'sm' | 'md' | 'lg';
export type AvatarAction = 'profile' | 'menu' | 'none';

@Component({
  selector: 'app-user-avatar',
  standalone: true,
  imports: [ MatMenuModule, MatIconModule],
  templateUrl: './user-avatar.html',
  styleUrl: './user-avatar.scss',
})
export class UserAvatar {
  private router = inject(Router);

  user = input.required<User | null>();
  size = input<AvatarSize>('md'); 
  
  action = input<AvatarAction>('profile'); 
  
  showName = input<boolean>(false);
  showHandle = input<boolean>(false);
  showEmail = input<boolean>(false);
  time = input<string | null>(null);

  avatarClick = output<MouseEvent>();

  navigateToProfile() {
    if (this.action() === 'profile' && this.user()?.id) {
      this.router.navigate(['/profile', this.user()?.id]);
    }
  }
}