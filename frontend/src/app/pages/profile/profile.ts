import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs';

// Material
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatChipsModule } from '@angular/material/chips';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatRippleModule } from '@angular/material/core';

import { AuthService } from '../../core/services/auth.service';
import { EditProfileDialogComponent } from '../../features/profile/components/edit-profile-dialog/edit-profile-dialog';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatTabsModule,
    MatDialogModule,
    MatChipsModule,
    MatCardModule,
    MatDividerModule,
    MatRippleModule
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile {
  private auth = inject(AuthService);
  private dialog = inject(MatDialog);
  private breakpointObserver = inject(BreakpointObserver);

  user = this.auth.user; 

  isOwn = computed(() => this.user()?.id !== this.auth.user()?.id);
  isAdmin = computed(() => this.user()?.role === 'admin');

  isMobile = toSignal(
    this.breakpointObserver
      .observe([Breakpoints.Handset])
      .pipe(map(result => result.matches)),
    { initialValue: false }
  );

  
  stats = signal({ posts: 42, followers: 3840, following: 672 });

  posts = signal([
    { id: 1, title: 'Deep dive into Angular Signals 2026', image: 'https://picsum.photos/seed/p1/800/400', date: 'Jan 18' },
    { id: 2, title: 'Building glassmorphic UIs', image: 'https://picsum.photos/seed/p2/800/400', date: 'Dec 30' },
  ]);

 openEditDialog(type: 'profile' | 'cover' | 'avatar' | 'about') {
  const dialogRef = this.dialog.open(EditProfileDialogComponent, {
    width: '500px',
    maxWidth: '95vw',
    data: {
      type: type,
      user: this.user() // Pass the current user signal value
    },
    autoFocus: false
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      console.log('User updated data:', result);
      // Here you would call: this.authService.updateProfile(result);
    }
  });
}

  toggleFollow() {
    // Logic for following
  }
}