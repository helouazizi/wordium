import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { routes } from './app.routes';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { ErrorInterceptor } from './core/interceptors/error.interceptor';
import { LoggingInterceptor } from './core/interceptors/logging.interceptor';
import { LoadingInterceptor } from './core/interceptors/loading.interceptor';
import { environment } from '../environments/environment';
import { Configuration as AuthConfig } from './core/api/auth/configuration';
import { AuthControllerService } from './core/api/auth/api/authController.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoggingInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true },

    // Auth service
    { provide: AuthConfig, useValue: new AuthConfig({ basePath: environment.apiUrl }) },
    AuthControllerService, // ore use     ...provideApi(() => new AuthConfig({ basePath: environment.apiAuth }))

    // posts service
  ],
};
