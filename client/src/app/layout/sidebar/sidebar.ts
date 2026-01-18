import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { SessionService } from '../../core/services/session.service';
import { UserProfile } from '../../shared/components/user-profile/user-profile';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, CommonModule, RouterLinkActive, UserProfile],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar implements OnInit {
  private session = inject(SessionService);
  user = this.session.getUser();
  userRole = this.user?.role;

  menuLinks: any[] = [];

  ngOnInit() {
    const baseLinks = [
      { path: '/', label: 'Feed', icon: 'bi-house' },
      { path: '/explore', label: 'Explore', icon: 'bi-compass' },
      { path: '/write', label: 'Create', icon: 'bi-plus-square' },
      { path: '/bookmarks', label: 'Saved', icon: 'bi-bookmark-heart' }, 
      { path: '/my-posts', label: 'Stories', icon: 'bi-journal-text' }, 
    ];

    // If admin, add dashboard to the start
    if (this.userRole?.toLowerCase() === 'admin') {
      this.menuLinks = [
        { path: '/dashboard', label: 'Dashboard', icon: 'bi-grid-1x2' },
        ...baseLinks
      ];
    } else {
      this.menuLinks = baseLinks;
    }
  }
}