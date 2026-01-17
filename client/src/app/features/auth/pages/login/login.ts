import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgIf } from '@angular/common';
import { LoginRequest } from '../../../../core/apis/auth/models';
import { ProblemDetail } from '../../../../shared/models/problem-detail';
import { SessionService } from '../../../../core/services/session.service';
import { AuthFacade } from '../../auth.facade';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  constructor(private cd: ChangeDetectorRef) {}
  private authFacade = inject(AuthFacade);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private session = inject(SessionService);

  form = this.fb.group({
    identifier: ['', [Validators.required]],
    password: ['', Validators.required],
  });

  errorMessage: string | null = null;
  isSubmitting = false;
  login() {
    const formValue = this.form.value;
    if (!formValue || this.form.invalid) return;
    this.isSubmitting = true;

    const { identifier, password } = formValue;

    const payload: LoginRequest = { email: null, username: null, password: password ?? '' };

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailPattern.test(identifier ?? '')) {
      payload.email = identifier ?? null;
    } else {
      payload.username = identifier ?? null;
    }

    this.errorMessage = null;

    this.authFacade.login(payload).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        const problem: ProblemDetail = err as ProblemDetail;
        this.errorMessage = problem?.detail || 'Login failed';
        this.isSubmitting = false;
        this.cd.markForCheck();
      },
      complete: () => {
        this.isSubmitting = false;
      },
    });
  }
}
