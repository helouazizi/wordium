import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notification = inject(NotificationService);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An unknown error occurred';

      if (error.error instanceof ErrorEvent) {
        // Client-side error (network, etc)
        errorMessage = `Error: ${error.error.message}`;
      } else {
        // Server-side error
        switch (error.error.status) {
          case 400:
            errorMessage = error.error?.detail || 'Bad Request';
            break;
          case 401:
            errorMessage = 'Session expired. Please login again.';
            authService.logout(); // Auto logout on unauthorized
            break;
          case 403:
            errorMessage = 'You do not have permission to perform this action.';
            break;
          case 404:
            errorMessage = 'The requested resource was not found.';
            break;
          case 500:
            
            errorMessage = 'Internal Server Error. Please try again later.';
            break;
          default:
            errorMessage = error.error?.message || `Error Code: ${error.status}`;
        }
      }

      // console.log(errorMessage,"dzfffffffffffffffffsss");
      
      notification.showError(errorMessage);
      return throwError(() => error);
    }),
  );
};
