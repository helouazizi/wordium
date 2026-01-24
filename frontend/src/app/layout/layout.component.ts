import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NAV_LINKS } from '../core/config/navigation.config';
import { ThemeService } from '../core/services/theme.service';
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle';
import { Header } from '../shared/components/header/header';
import { DeviceService } from '../core/services/device.service';

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
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatButtonToggleGroup,
    MatButtonToggle,
    Header,
  ],
})
export class LayoutComponent {
  public themeService = inject(ThemeService);
  private device = inject(DeviceService)
  navLinks = NAV_LINKS;
  isHandset$ = this.device.isHandset;

  toggleTheme() {
    this.themeService.toggle();
  }
}
