import { inject, Injectable, signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { SessionService } from '../../core/services/session.service';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest, SignupRequest } from '../../core/apis/auth/models';
import { catchError, finalize, map, Observable, throwError } from 'rxjs';
import { ProblemDetail } from '../../shared/models/problem-detail';

@Injectable({ providedIn: 'root' })
export class AuthFacade {
  private readonly loading = signal(false);
  private readonly error = signal<string | null>(null);
  private readonly fieldErrors = signal<Record<string, string>>({});

  readonly loading$ = toObservable(this.loading);
  readonly error$ = toObservable(this.error);
  readonly fieldErrors$ = toObservable(this.fieldErrors);

  private session = inject(SessionService);
  private router = inject(Router);
  private authService = inject(AuthService);

  readonly user$ = this.session.user$;

  constructor() {
    this.router.events.subscribe(() => this.clearErrors());
  }

  signup(payload: SignupRequest): Observable<void> {
    this.resetState();

    return this.authService.signup(payload).pipe(
      map(() => void 0),
      catchError((err) => {
        this.handleProblemDetail(err);
        return throwError(() => err);
      }),
      finalize(() => this.loading.set(false))
    );
  }

  login(payload: LoginRequest): Observable<void> {
    this.resetState();

    return this.authService.login(payload).pipe(
      map(() => void 0),
      catchError((err) => {
        this.handleProblemDetail(err);
        return throwError(() => err);
      }),
      finalize(() => this.loading.set(false))
    );
  }

  logout(): void {
    this.authService.logout();
    this.clearErrors();
  }

  private resetState() {
    this.loading.set(true);
    this.error.set(null);
    this.fieldErrors.set({});
  }

  private handleProblemDetail(err: unknown) {
    const problem = err as ProblemDetail;

    this.error.set(problem?.detail ?? problem?.title ?? 'Request failed');

    if (Array.isArray(problem?.fieldErrors)) {
      const fieldErrorMap: Record<string, string> = {};

      problem.fieldErrors.forEach((item) => {
        if (item.field && item.message) {
          fieldErrorMap[item.field] = item.message;
        }
      });

      this.fieldErrors.set(fieldErrorMap);
    } else {
      this.fieldErrors.set({});
    }
  }

  clearErrors() {
    this.error.set(null);
    this.fieldErrors.set({});
  }
}
