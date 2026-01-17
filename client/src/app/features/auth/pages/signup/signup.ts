import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SignupRequest } from '../../../../core/apis/auth/models';
import { User } from '../../../../shared/models/user';
import { Component, effect, inject } from '@angular/core';
import { AuthFacade } from '../../auth.facade';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.html',
  styleUrls: ['./signup.scss'],
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
})
export class Signup {
  private fb = inject(FormBuilder);
  private auth = inject(AuthFacade);
  private router = inject(Router);

  // Connect to Facade signals
  readonly loading = this.auth.loading;
  readonly error = this.auth.error;
  readonly fieldErrors = this.auth.fieldErrors;
  readonly isLoggedIn = this.auth.isLoggedIn;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: ['', Validators.required],
    bio: [''],
    location: [''],
    avatar: [null as File | null],
  });

  avatarPreview: string | ArrayBuffer | null = null;

  constructor() {
    effect(() => {
      const errors = this.fieldErrors();
      this.applyBackendErrors(this.form, errors);
    });

    effect(() => {
      if (this.isLoggedIn()) {
        this.router.navigate(['/']);
      }
    });
  }

  onAvatarChange(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (!file) return;
    this.form.patchValue({ avatar: file });
    const reader = new FileReader();
    reader.onload = () => (this.avatarPreview = reader.result);
    reader.readAsDataURL(file);
  }

  signup() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.form.getRawValue() as SignupRequest;

    this.auth.signup(payload).subscribe({
      next: (user: User) => {
        // console.log('Signup successful, user state updated:', user);
        // Navigation is handled by the effect() above,
        // or you can manually call this.router.navigate(['/']) here.
      },
    });
  }

  private applyBackendErrors(form: FormGroup, errors: Record<string, string>) {
    Object.entries(errors).forEach(([field, message]) => {
      const control = form.get(field);
      if (control) {
        control.setErrors({ backend: message });
      }
    });
  }
}
