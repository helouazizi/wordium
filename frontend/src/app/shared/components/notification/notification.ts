import { Component, Input } from '@angular/core';
import { NotificationInter } from '../../../core/apis/notifications/notification.model';
import { UserAvatar } from '../user-avatar/user-avatar';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-notification',
  imports: [UserAvatar,MatIcon],
  templateUrl: './notification.html',
  styleUrl: './notification.scss',
})
export class Notification {
  @Input() notification!: NotificationInter;

  markAsRead() {
    this.notification.read = true;
  }

  get message(): string {
    switch (this.notification.type) {
      case 'FOLLOW':
        return 'started following you';
      case 'UNFOLLOW':
        return 'stopped following you';
      case 'POST':
        return 'created a new post';
      default:
        return '';
    }
  }

  get icon(): string {
    switch (this.notification.type) {
      case 'FOLLOW':
        return 'person_add';
      case 'UNFOLLOW':
        return 'person_remove';
      case 'POST':
        return 'post_add';
      default:
        return 'notifications';
    }
  }
}
