import { Component, inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AsyncPipe } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Header } from '../theme/components/header/header';
import { NAV_LINKS } from '../core/config/navigation.config';
import { ThemeService } from '../core/services/theme.service';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    AsyncPipe,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    // MatSlideToggle,
    MatButtonToggleGroup,
    MatButtonToggle,
    Header,
  ],
})
export class LayoutComponent {
  private breakpointObserver = inject(BreakpointObserver);
  public themeService = inject(ThemeService);
  navLinks = NAV_LINKS;
  isHandset$: Observable<boolean> = this.breakpointObserver.observe(['(max-width: 767px)']).pipe(
    map((result) => result.matches),
    shareReplay(),
  );

  toggleTheme() {
    this.themeService.toggle();
  }
}
