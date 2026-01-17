import { Injectable, signal } from '@angular/core';
import { Toast, ToastType } from '../../shared/models/toast.model';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private _toasts = signal<Toast[]>([]);
  readonly toasts = this._toasts.asReadonly();

  show(message: string, type: ToastType = 'info', title?: string) {
    const id = Date.now();
    const newToast: Toast = { id, message, type, title };

    this._toasts.update((toasts) => [...toasts, newToast]);

    setTimeout(() => this.remove(id), 5000);
  }

  remove(id: number) {
    this._toasts.update((toasts) => toasts.filter((t) => t.id !== id));
  }

  success(msg: string, title = 'Success') { this.show(msg, 'success', title); }
  error(msg: string, title = 'Error') { this.show(msg, 'error', title); }
}