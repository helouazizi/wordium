import { inject, Injectable, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { SessionService } from '../../core/services/session.service';
import { LoginRequest, SignupRequest } from '../../core/apis/auth/models';
import { ProblemDetail } from '../../shared/models/problem-detail';

@Injectable({ providedIn: 'root' })
export class AuthFacade {
  private authService = inject(AuthService);
  private session = inject(SessionService);
  private router = inject(Router);

  private _loading = signal(false);
  private _error = signal<string | null>(null);
  private _fieldErrors = signal<Record<string, string>>({});

  readonly loading = this._loading.asReadonly();
  readonly error = this._error.asReadonly();
  readonly fieldErrors = this._fieldErrors.asReadonly();

  readonly user = this.session.user; // already a signal
  readonly isLoggedIn = this.session.isLoggedIn;

  constructor() {
    this.router.events.subscribe(() => this.clearErrors());
  }

  signup(payload: SignupRequest) {
    this.resetState();

    this.authService.signup(payload).subscribe({
      next: () => {},
      error: (err) => this.handleProblemDetail(err),
      complete: () => this._loading.set(false),
    });
  }

  login(payload: LoginRequest) {
    this.resetState();

    this.authService.login(payload).subscribe({
      next: () => {},
      error: (err) => this.handleProblemDetail(err),
      complete: () => this._loading.set(false),
    });
  }

  logout() {
    this.authService.logout();
    this.clearErrors();
  }

  private resetState() {
    this._loading.set(true);
    this._error.set(null);
    this._fieldErrors.set({});
  }

  private handleProblemDetail(err: unknown) {
    const problem = err as ProblemDetail;

    this._error.set(problem?.detail ?? problem?.title ?? 'Request failed');

    if (Array.isArray(problem?.fieldErrors)) {
      const map: Record<string, string> = {};

      for (const item of problem.fieldErrors) {
        if (item.field && item.message) {
          map[item.field] = item.message;
        }
      }

      this._fieldErrors.set(map);
    } else {
      this._fieldErrors.set({});
    }

    this._loading.set(false);
  }

  clearErrors() {
    this._error.set(null);
    this._fieldErrors.set({});
  }
}
