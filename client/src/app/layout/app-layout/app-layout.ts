import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
// import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { ResponsiveService } from '../../core/services/responsive.service';

@Component({
  selector: 'app-app-layout',
  imports: [CommonModule, RouterModule],
  templateUrl: './app-layout.html',
  styleUrl: './app-layout.scss',
})
export class AppLayout {
  constructor(private responsive: ResponsiveService) {}
  isMobile = false;
  sidebarOpen = false;
  ngOnInit() {
    this.responsive.isMobile$.subscribe((isMobile) => {
      this.isMobile = isMobile;
      if (!isMobile) this.sidebarOpen = true;
    });
  }
}

//  example usage

// <aside class="sidebar" [class.hidden]="isMobile">
//   <!-- sidebar content -->
// </aside>

// <main class="main-content">
//   <header>
//     <button *ngIf="isMobile" (click)="toggleSidebar()">☰</button>
//     <app-user-profile></app-user-profile>
//   </header>
//   <router-outlet></router-outlet>
// </main>
