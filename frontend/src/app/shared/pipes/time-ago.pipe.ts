import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'timeAgo', standalone: true })
export class TimeAgo implements PipeTransform {
  transform(value: string | Date | null | undefined): string {
    if (!value) return '';

    let dateValue: Date;

    if (value instanceof Date) {
      dateValue = value;
    } else {
      const dateStr =
        typeof value === 'string' && !value.includes('Z') && !value.includes('+')
          ? `${value}Z`
          : value;
      dateValue = new Date(dateStr);
    }

    if (isNaN(dateValue.getTime())) {
      return '';
    }

    const now = new Date();
    const diffMs = now.getTime() - dateValue.getTime();
    const diffSeconds = Math.floor(diffMs / 1000);

    if (diffSeconds < 30) return 'Just now';

    if (diffSeconds < 60) return `${diffSeconds}s ago`;
    if (diffSeconds < 3600) return `${Math.floor(diffSeconds / 60)}m ago`;
    if (diffSeconds < 86400) return `${Math.floor(diffSeconds / 3600)}h ago`;

    const days = Math.floor(diffSeconds / 86400);
    if (days < 7) return `${days}d ago`;

    return dateValue.toLocaleDateString();
  }
}
