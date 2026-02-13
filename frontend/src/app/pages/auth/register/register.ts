import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { passwordMatchValidator } from '../../../shared/validators/password-match.validator';
import { AuthService } from '../../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NotificationService } from '../../../core/services/notification.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    RouterLink,
    MatProgressSpinnerModule,
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private notification = inject(NotificationService);

  isLoading = signal(false);
  isLoadingAvatar = signal(false);
  hidePassword = signal(true);
  avatarPreview = signal<string | null>(null);
  errorDetail = signal('');

  registerForm = this.fb.nonNullable.group(
    {
      username: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      bio: ['', [Validators.maxLength(160)]],
      location: [''],
      avatar: [''],
      avatarPublicId: [''],
    },
    { validators: passwordMatchValidator },
  );

  async onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      const valid = await this.isValidMedia(file);
      if (!valid) {
        this.notification.showError('Only PNG, JPG, and WEBP images are allowed.');
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        alert('File is too large. Please choose an image under 2MB.');
        return;
      }
      this.isLoadingAvatar.set(true);
      this.authService.uploadImage(file).subscribe({
        next: (res) => {
          this.registerForm.patchValue({ avatar: res.secure_url, avatarPublicId: res.public_id });
          this.isLoadingAvatar.set(false);
          this.notification.showSuccess('Avatar saved');
        },
        error: (err) => {
          this.isLoadingAvatar.set(false);
          this.notification.showError('Failed to save avatar');
          this.avatarPreview.set('');
        },
      });

      const reader = new FileReader();
      reader.onload = () => this.avatarPreview.set(reader.result as string);
      reader.readAsDataURL(file);
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

  removeAvatar(event?: Event) {
    if (event) event.stopPropagation();
    this.registerForm.patchValue({ avatar: '' });
    this.avatarPreview.set(null);
  }
  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading.set(true);
      const formValues = this.registerForm.getRawValue();
      const { confirmPassword, ...data } = formValues;
      this.authService.register(data).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.notification.showSuccess('Account Created with succes');
        },
        error: (error: HttpErrorResponse) => {
          this.isLoading.set(false);
          this.errorDetail.set(error.error.detail);

          if (error.status === 400 && Array.isArray(error.error?.fieldErrors)) {
            const fieldErrors = error.error.fieldErrors;

            fieldErrors.forEach((err: { field: string; message: string }) => {
              const control = this.registerForm.get(err.field);
              if (control) {
                control.setErrors({ serverError: err.message });
              }
            });
          }
          this.notification.showError('Creation Faild');
        },
      });
    }
  }
}
