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
    MatProgressSpinnerModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);

  isLoading = signal(false);
  hidePassword = signal(true);
  avatarPreview = signal<string | null>(null);

  registerForm = this.fb.nonNullable.group(
    {
      username: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      bio: ['', [Validators.maxLength(160)]],
      location: [''],
      avatar: [null as File | null],
    },
    { validators: passwordMatchValidator },
  );

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.registerForm.patchValue({ avatar: file });

      const reader = new FileReader();
      reader.onload = () => this.avatarPreview.set(reader.result as string);
      reader.readAsDataURL(file);
    }
  }

  removeAvatar(event?: Event) {
    if (event) event.stopPropagation();
    this.registerForm.patchValue({ avatar: null });
    this.avatarPreview.set(null);
  }
  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading.set(true);
      const formValues = this.registerForm.getRawValue();
      const { confirmPassword, ...data } = formValues;

      this.authService.register(data).subscribe({
        next: () => this.isLoading.set(false),
        error: (error: HttpErrorResponse) => {
          this.isLoading.set(false);

          if (error.status === 400 && Array.isArray(error.error?.fieldErrors)) {
            const fieldErrors = error.error.fieldErrors;

            fieldErrors.forEach((err: { field: string; message: string }) => {
              const control = this.registerForm.get(err.field);
              if (control) {
                control.setErrors({ serverError: err.message });
              }
            });
          }
        },
      });
    }
  }
}
