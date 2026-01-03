import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ResponsiveService } from '../../core/services/responsive.service';
import { MaterialModule } from '../../shared/material.module';
import { SessionService } from '../../core/services/session.service';
import { UserProfile } from '../../shared/components/user-profile/user-profile'; 

@Component({
  selector: 'app-app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, UserProfile],
  templateUrl: './app-layout.html',
  styleUrls: ['./app-layout.scss'],
})
export class AppLayout {
  isMobile = false;
  user$ = inject(SessionService).user$;

  constructor(private responsive: ResponsiveService) {}

  ngOnInit() {
    this.responsive.isMobile$.subscribe(isMobile => {
      this.isMobile = isMobile;
    });
  }

  goToProfile(userId: string) {
    // navigate to profile page
  }
}
