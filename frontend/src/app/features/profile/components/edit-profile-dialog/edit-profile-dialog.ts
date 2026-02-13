import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  UntypedFormBuilder,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { User } from '../../../../shared/models/user.model';
import { UsersService } from '../../../../core/services/users.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { MatSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-edit-profile-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    MatSpinner,
  ],
  templateUrl: './edit-profile-dialog.html',
  styleUrls: ['./edit-profile-dialog.scss'],
})
export class EditProfileDialogComponent {
  private usersService = inject(UsersService);
  private notification = inject(NotificationService);

  isLoadingAvatar = signal(false);
  private dialogRef = inject(MatDialogRef<EditProfileDialogComponent>);
  public data = inject<{ type: 'profile' | 'cover' | 'avatar' | 'about' | 'social'; user: User }>(
    MAT_DIALOG_DATA,
  );
  private fb = inject(UntypedFormBuilder);

  editForm = this.fb.group({
    displayName: [this.data.user.displayName],
    bio: [this.data.user.bio],
    location: [this.data.user.location],
    avatar: [this.data.user.avatar],
    cover: [this.data.user.cover],
    avatarPublicId: [''],
    coverPublicId: [''],
    social: this.fb.group({
      website: [this.data.user.social?.website || ''],
      twitter: [this.data.user.social?.twitter || ''],
      github: [this.data.user.social?.github || ''],
      linkedin: [this.data.user.social?.linkedin || ''],
    }),
  });
  save() {
    if (this.editForm.valid) {
      const result: any = {};
      switch (this.data.type) {
        case 'profile':
          result.displayName = this.editForm.value.displayName;
          result.location = this.editForm.value.location;
          break;
        case 'about':
          result.bio = this.editForm.value.bio;
          break;
        case 'avatar':
          result.avatar = this.editForm.value.avatar;
          result.avatarPublicId = this.editForm.value.avatarPublicId;
          break;
        case 'cover':
          result.cover = this.editForm.value.cover;
          result.coverPublicId = this.editForm.value.coverPublicId;
          break;
        case 'social':
          result.social = { ...this.editForm.value.social };
          break;
      }

      this.dialogRef.close(result);
    }
  }

  get socialForm() {
    return this.editForm.get('social') as FormGroup;
  }

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files[0]) {
      const file = input.files[0];

      const valid = await this.isValidMedia(file);
      if (!valid) {
        this.notification.showError('Only PNG, JPG, and WEBP images are allowed.');
        return;
      }

      if (file.size > 2 * 1024 * 1024) {
        this.notification.showError('File is too large. Please choose an image under 2MB.');
        return;
      }

      this.isLoadingAvatar.set(true);
      this.usersService.uploadImage(file).subscribe({
        next: (res) => {
          if (this.data.type === 'avatar') {
            this.editForm.patchValue({ avatar: res.secure_url, avatarPublicId: res.public_id });
          } else if (this.data.type === 'cover') {
            this.editForm.patchValue({ cover: res.secure_url, coverPublicId: res.public_id });
          }
          this.isLoadingAvatar.set(false);
          this.notification.showSuccess('Media saved');
        },
        error: (err) => {
          this.isLoadingAvatar.set(false);
          this.notification.showError('Failed to save media');
        },
      });
    }
  }
  private async isValidMedia(file: File): Promise<boolean> {
    const allowedImageHeaders: Record<string, string[]> = {
      png: ['89504e47'], // PNG
      jpg: ['ffd8ff'], // JPEG
      jpeg: ['ffd8ff'], // JPEG
      webp: ['52494646'], // WEBP (RIFF)
    };

    // Read first 12 bytes of the file
    const buffer = await file.arrayBuffer();
    const arr = new Uint8Array(buffer.slice(0, 12));
    const header = Array.from(arr)
      .map((b) => b.toString(16).padStart(2, '0'))
      .join('');

    // Check against allowed images only
    for (const headers of Object.values(allowedImageHeaders)) {
      if (headers.some((h) => header.startsWith(h))) return true;
    }

    return false;
  }
  getIcon(): string {
    switch (this.data.type) {
      case 'profile':
        return 'account_circle';
      case 'avatar':
        return 'face';
      case 'cover':
        return 'image';
      case 'about':
        return 'description';
      case 'social':
        return 'share';
      default:
        return 'edit';
    }
  }

  close() {
    this.dialogRef.close();
  }
}
