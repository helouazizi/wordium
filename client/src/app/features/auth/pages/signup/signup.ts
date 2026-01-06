import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SignupRequest } from '../../../../core/apis/auth/models';
import { AuthFacade } from '../../auth.facade';
@Component({
  selector: 'app-signup',
  standalone: true,
  templateUrl: './signup.html',
  styleUrls: ['./signup.scss'],
  imports: [ReactiveFormsModule, NgIf, RouterLink, CommonModule],
})
export class Signup {
  private fb = inject(FormBuilder);
  private authFacade = inject(AuthFacade);
  private router = inject(Router);

  loading$ = this.authFacade.loading$;
  error$ = this.authFacade.error$;
  fieldErrors$ = this.authFacade.fieldErrors$;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: ['', Validators.required],
    bio: [''],
    location: [''],
    avatar: [null as File | null],
  });

  avatarPreview: string | ArrayBuffer | null = null;

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

    this.authFacade.signup(this.form.getRawValue() as SignupRequest).subscribe({
      next: () => this.router.navigate(['/']),
    });
  }

  ngOnInit() {
    this.fieldErrors$.subscribe((errors) => {
      this.applyBackendErrors(this.form, errors);
    });
  }

  private applyBackendErrors(form: FormGroup, errors: Record<string, string>) {
    Object.entries(errors).forEach(([field, message]) => {
      const control = form.get(field);
      if (control) {
        control.setErrors({ ...control.errors, backend: message });
      }
    });
  }
}
