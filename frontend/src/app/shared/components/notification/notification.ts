import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NotificationInter } from '../../../core/apis/notifications/notification.model';
import { UserAvatar } from '../user-avatar/user-avatar';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-notification',
  imports: [UserAvatar, MatIcon],
  templateUrl: './notification.html',
  styleUrl: './notification.scss',
})
export class Notification {
  @Input() notification!: NotificationInter;
  @Output() read = new EventEmitter<number>();

  markAsRead() {
    this.read.emit(this.notification.id);
  }

  get message(): string {
    switch (this.notification.type) {
      case 'FOLLOW':
        return 'started following you';
      case 'UNFOLLOW':
        return 'stopped following you';
      case 'POST_CREATED':
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
      case 'POST_CREATED':
        return 'post_add';
      default:
        return 'notifications';
    }
  }
}
