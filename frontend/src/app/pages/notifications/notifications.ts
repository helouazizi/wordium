import { DatePipe } from '@angular/common';
import { Component, signal } from '@angular/core';
interface Notification {
  message: string;
  date: Date;
}
@Component({
  selector: 'app-notifications',
  imports: [DatePipe],
  templateUrl: './notifications.html',
  styleUrl: './notifications.scss',
})
export class Notifications {
  notifications = signal<Notification[]>([
    { message: 'Welcome to Wordium!', date: new Date() },
    { message: 'Your post was liked', date: new Date() },
  ]);
}
