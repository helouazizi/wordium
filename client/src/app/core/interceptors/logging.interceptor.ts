import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { tap } from 'rxjs';

export class LoggingInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    console.log('HTTP Request:', req.url);
    return next.handle(req).pipe(tap((event) => console.log('HTTP Response:', event)));
  }
}
