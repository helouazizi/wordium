import { User } from '../../../shared/models/user.model';
type NotificationType = 'FOLLOW' | 'UNFOLLOW' | 'POST_CREATED';
export interface NotificationInter {
  id: number;
  type: NotificationType;
  actor: User;
  unreadCount: number;
  totalCount: number;
  read: boolean;
  createdAt: string;
}
