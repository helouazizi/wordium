import { Component, inject, signal } from '@angular/core';
import { NotificationsService } from '../../core/services/notifications.service';
import { NotificationInter } from '../../core/apis/notifications/notification.model';
import { Notification } from '../../shared/components/notification/notification';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-notifications',
  imports: [Notification, MatIcon],
  templateUrl: './notifications.html',
  styleUrl: './notifications.scss',
})
export class Notifications {
  private notificationService = inject(NotificationsService);

  notifications = signal<NotificationInter[]>([]);

  constructor() {
    this.loadNotifications();
  }

  private loadNotifications() {
    this.notificationService.getNotifications().subscribe({
      next: (data: any) => {
        console.log(data);
        this.notifications.set(data.notifications);
      },
    });
  }

  markAsRead(notificationId: number) {
    // Update local signal immediately
    this.notificationService.notifCount.update((c) => c - 1);
    this.notifications.update((current) =>
      current.map((n) => (n.id === notificationId ? { ...n, read: true } : n)),
    );

    // Optional: send update to backend
    this.notificationService.markAsRead(notificationId).subscribe();
  }
}
