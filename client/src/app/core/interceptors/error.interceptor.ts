import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { catchError, throwError } from "rxjs";

export class ErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return next.handle(req).pipe(
      catchError((error) => {
        if (error.status === 401) {
          // redirect to login
        } else {
          // show toast/error modal
        }
        return throwError(() => error);
      })
    );
  }
}
