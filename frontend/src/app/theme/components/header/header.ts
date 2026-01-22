import { Component, inject, input, output } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { UserAvatar } from '../../../shared/components/user-avatar/user-avatar';
import { MatDivider } from '@angular/material/divider';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

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
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  session = inject(AuthService);
  user = this.session.user();
  router = inject(Router)


  logout() {
    this.session.logout();
  }

   profile() {
    this.router.navigate(['/profiles/me']);
  }
}
