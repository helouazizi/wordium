import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { finalize } from 'rxjs';

export class LoadingInterceptor implements HttpInterceptor {
  //   constructor(private loader: LoaderService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    // this.loader.show();
    return next
      .handle(req)
      .pipe
      //   finalize(() => this.loader.hide())
      ();
  }
}
