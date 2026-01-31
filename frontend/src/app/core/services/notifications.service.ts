import { inject, Injectable } from '@angular/core';
import { PageRequest } from '../../shared/models/pagination.model';
import { NotificationsClient } from '../apis/notifications/notifications.client';

@Injectable({
  providedIn: 'root',
})
export class NotificationsService {
  private readonly client = inject(NotificationsClient);
  getNotifications(params?: PageRequest) {
    return this.client.getNotifications(params);
  }
}
