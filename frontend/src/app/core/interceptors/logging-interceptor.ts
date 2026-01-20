
import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  if (environment.production) return next(req);

  const startTime = Date.now();
  
  return next(req).pipe(
    tap({
      next: (event) => {
        if (event instanceof HttpResponse) {
          const elapsed = Date.now() - startTime;
          console.log(`%c [API] ${req.method} ${req.urlWithParams} - ${elapsed}ms`, 'color: #4CAF50');
        }
      },
      error: (err) => {
        console.error(`%c [API ERROR] ${req.method} ${req.urlWithParams}`, 'color: #F44336', err);
      }
    })
  );
};