import { ErrorHandler, Injectable, inject } from '@angular/core';
import { NotificationService } from '../services/notification.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  // Use Injector because ErrorHandler is loaded before other services
  private notification = inject(NotificationService);

  handleError(error: any): void {
    // 1. Log to console for developers
    console.error('Global Error Handler:', error);

    // 2. Here you would send the error to a service like Sentry or LogRocket
    // sendToLoggingService(error);

    // 3. Notify the user
    // this.notification.showError('A client-side error occurred. Please refresh the page.');
  }
}