import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UserProfile } from '../../shared/components/user-profile/user-profile';
import { SessionService } from '../../core/services/session.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [RouterLink, UserProfile,CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  private session = inject(SessionService);
  user$ = this.session.user$;

  
  private theme: 'light' | 'dark' = 'light';

  isDark = () => this.theme === 'dark';

  toggleTheme() {
    this.theme = this.theme === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-bs-theme', this.theme);
  }

  logout() {
    this.session.logout();
  }

  openNotifications() {
    console.log('Open notifications panel');
  }
}
