import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Observable, combineLatest, map } from 'rxjs';
import { ResponsiveService } from '../../core/services/responsive.service';
import { SessionService } from '../../core/services/session.service';
import { ThemeService } from '../../core/services/theme.service';
import { User } from '../../shared/models/user';
import { Header } from '../header/header';
import { Sidebar } from '../sidebar/sidebar';
@Component({
  selector: 'app-app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule,Header,Sidebar],
  templateUrl: './app-layout.html',
})
export class AppLayout {
  private responsive = inject(ResponsiveService);
  private session = inject(SessionService);
  private theme = inject(ThemeService);

  readonly user$: Observable<User | null> = this.session.user$;
  readonly isMobile = this.responsive.isHandset$;
  readonly isTablet = this.responsive.isTablet$;

  sidebarOpen = true;
  isDark = false;

  ngOnInit() {
    this.theme.theme$.subscribe((t) => (this.isDark = t === 'dark'));
  }

  toggleTheme() {
    this.theme.toggle();
  }

  logout() {
    this.session.clear();
  }
}
