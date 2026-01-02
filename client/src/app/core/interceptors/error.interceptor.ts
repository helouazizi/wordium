import { Injectable, NgZone } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ProblemDetail } from '../../models/problem-detail';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
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
