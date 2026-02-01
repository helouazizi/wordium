import { inject, Injectable, signal } from '@angular/core';
import { PageRequest } from '../../shared/models/pagination.model';
import { NotificationsClient } from '../apis/notifications/notifications.client';

@Injectable({
  providedIn: 'root', // singleton, available globally
})
export class NotificationsService {
  private readonly client = inject(NotificationsClient);

  notifCount = signal(0);

  constructor() {
    this.fetchUnreadCount();
  }

  getNotifications(params?: PageRequest) {
    return this.client.getNotifications(params);
  }

  markAsRead(id: number) {
    return this.client.markAsRead(id);
  }

  unReadCount() {
    return this.client.unReadCount();
  }

  fetchUnreadCount() {
    this.unReadCount().subscribe({
      next: (res: number) => this.notifCount.set(res),
      error: (err) => console.error('Failed to fetch unread count', err),
    });
  }
}
