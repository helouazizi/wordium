import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

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
import { UsersService } from '../../core/services/users.service';
import { DeviceService } from '../../core/services/device.service';
import { ActivatedRoute } from '@angular/router';
import { UpdateProfileRequest } from '../../core/apis/users/users.model';
import { User } from '../../shared/models/user.model';
import { PostList } from '../../shared/components/post-list/post-list';
import { NotFound } from '../../shared/components/not-found/not-found';

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
    MatRippleModule,
    PostList,
    NotFound
  ],
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss'],
})
export class Profile {
  private auth = inject(AuthService);
  private usersService = inject(UsersService);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);
  private device = inject(DeviceService);

  isMobile = this.device.isHandset;

  readonly sessionUser = this.auth.user;

  readonly targetUser = signal<User | null>(null);

  isOwn = computed(() => this.targetUser()?.id === this.sessionUser()?.id);
  isAdmin = computed(() => this.sessionUser()?.role === 'ADMIN');
  err = signal<string|null>(null)

  ngOnInit() {
    this.route.paramMap.subscribe((pm) => {
      const id =  Number(pm.get('id')) ? Number(pm.get('id')) : this.sessionUser()?.id;
      if (id) {
        this.usersService.getUserProfile(id).subscribe({
          next: (user) => {
            (this.targetUser.set(user), console.log(user, 'profile'));
          },
          error: (err) => {
            this.err.set(err.error.detail)
            console.log(err);
            console.log(this.err());
          }
        });
      }
    });

  }

  isSocialEmpty(social: User['social'] | undefined): boolean {
    if (!social) return true;
    return !social.website && !social.twitter && !social.github && !social.linkedin;
  }

  openEditDialog(type: 'profile' | 'cover' | 'avatar' | 'about' | 'social') {
    if (!this.isOwn()) return;

    const dialogRef = this.dialog.open(EditProfileDialogComponent, {
      width: '500px',
      maxWidth: '95vw',
      data: {
        type,
        user: this.targetUser(),
      },
      autoFocus: false,
    });

    dialogRef.afterClosed().subscribe((result: UpdateProfileRequest | undefined) => {
      if (result) {
        this.usersService.updateMyProfile(result as UpdateProfileRequest).subscribe({
          next: (updated) => this.targetUser.set(updated),
        });
      }
    });
  }

  toggleFollow() {
    const user = this.targetUser();
    if (!user || this.isOwn()) return;

    this.usersService.followUser(user.id).subscribe({
      next: () => {
        const current = this.targetUser();
        if (current) {
          const newTarget = { ...current, isFollowing: !current.isFollowing };
          this.targetUser.set(newTarget);
        }
      },
    });
  }
}
