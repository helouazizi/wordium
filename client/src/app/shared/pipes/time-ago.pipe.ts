import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'timeAgo', standalone: true })
export class TimeAgo implements PipeTransform {
  transform(value: string | Date): string {
    if (!value) return '';

    const date = new Date(value);
    const utcDate = new Date(value + 'Z');

    const now = new Date();

    const diffMs = now.getTime() - utcDate.getTime();
    const diff = Math.floor(diffMs / 1000);

    if (diff < 0) return 'in the future?'; // safety

    if (diff < 60) return 'Just now';
    if (diff < 60 * 60) return `${Math.floor(diff / 60)} min ago`;
    if (diff < 60 * 60 * 24) return `${Math.floor(diff / 3600)} h ago`;
    return `${Math.floor(diff / (3600 * 24))} days ago`;
  }
}
