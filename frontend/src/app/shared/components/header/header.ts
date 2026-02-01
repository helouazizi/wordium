import { Component, inject, input, output, signal } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { UserAvatar } from '../../../shared/components/user-avatar/user-avatar';
import { MatDivider } from '@angular/material/divider';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { NotificationsService } from '../../../core/services/notifications.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    UserAvatar,
    MatDivider,
    RouterLink,
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  session = inject(AuthService);
  user = this.session.user();
  router = inject(Router);
  notifications = inject(NotificationsService);

  notifCount = this.notifications.notifCount;



  logout() {
    this.session.logout();
  }

  profile() {
    this.router.navigate(['/profiles/', this.user?.id]);
  }
}
