import { inject, Injectable, NgZone } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ProblemDetail } from '../../shared/models/problem-detail';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  private session = inject(SessionService);
  private router = inject(Router);
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        // in case of 401 redirect and clear session
        if (error.status === 401) {
          this.session.clear();
          this.router.navigate(['/login']);
        }
        const problem: ProblemDetail =
          error.error && typeof error.error === 'object'
            ? error.error
            : {
                title: 'Unknown error',
                detail: error.message,
                status: error.status,
              };

        return throwError(() => problem);
      })
    );
  }
}
