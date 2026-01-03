import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgIf } from '@angular/common';
import { LoginRequest } from '../../../../core/apis/auth/models';
import { ProblemDetail } from '../../../../shared/models/problem-detail';
import { SessionService } from '../../../../core/services/session.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  constructor(private cd: ChangeDetectorRef) {}
  private auth = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private session = inject(SessionService);

  form = this.fb.group({
    identifier: ['', [Validators.required]],
    password: ['', Validators.required],
  });

  errorMessage: string | null = null;

  login() {
    const formValue = this.form.value;
    if (!formValue || this.form.invalid) return;

    const { identifier, password } = formValue;

    const payload: LoginRequest = { email: null, username: null, password: password ?? '' };

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailPattern.test(identifier ?? '')) {
      payload.email = identifier ?? null;
    } else {
      payload.username = identifier ?? null;
    }

    this.errorMessage = null;

    this.auth.login(payload).subscribe({
      next: () => {
        this.router.navigate(['']);
      },
      error: (err) => {
        const problem: ProblemDetail = err as ProblemDetail;
        this.errorMessage = problem?.detail || 'Login failed';
        this.cd.markForCheck();
      },
    });
  }
}
