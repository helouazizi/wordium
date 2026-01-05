import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SignupRequest } from '../../../../core/apis/auth/models';
import { AuthService } from '../../../../core/services/auth.service';
import { ProblemDetail } from '../../../../shared/models/problem-detail';
import { Loading } from '../../../../shared/components/global-loader/global-loader';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.html',
  styleUrls: ['./signup.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, RouterLink],
})
export class Signup {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: ['', Validators.required],
    bio: [''],
    location: [''],
    avatar: [null as File | null],
  });

  errorMessage: string | null = null;
  avatarPreview: string | ArrayBuffer | null = null;
  isSubmitting = false;
  onAvatarChange(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      this.form.patchValue({ avatar: file });
      const reader = new FileReader();
      reader.onload = () => (this.avatarPreview = reader.result);
      reader.readAsDataURL(file);
      this.cd.markForCheck();
    }
  }

  signup() {
    if (this.form.invalid) return;
    this.isSubmitting = true;
    this.errorMessage = null;
    const formValue = this.form.value;

    const payload: SignupRequest = {
      email: formValue.email!,
      username: formValue.username!,
      password: formValue.password!,
      bio: formValue.bio || undefined,
      location: formValue.location || undefined,
      avatar: formValue.avatar || undefined,
    };

    this.auth.signup(payload).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err: any) => {
        const problem: ProblemDetail = err as ProblemDetail;
        this.isSubmitting = false;

        this.errorMessage = problem?.detail || 'Signup failed';

        if (problem?.fieldErrors && Array.isArray(problem.fieldErrors)) {
          (problem.fieldErrors as any[]).forEach((fe) => {
            const control = this.form.get(fe.field);
            if (control) {
              control.setErrors({ backend: fe.message });
            }
          });
        }

        this.cd.markForCheck();
      },
      complete: () => {
        this.isSubmitting = false;
      },
    });
  }
}
