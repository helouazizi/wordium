import { Component, input, output, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';

export type ReportType = 'user' | 'post';

@Component({
  selector: 'app-report-card',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatChipsModule, MatTooltipModule],
  templateUrl: './report-card.html',
  styleUrl: './report-card.scss'
})
export class ReportCard {
  report = input.required<any>();
  type = input.required<ReportType>();

  onResolve = output<any>();
  onIgnore = output<any>();
  onInspect = output<any>();

  // Creative Logic: Determine severity color based on report count
  severityClass = computed(() => {
    const count = this.report().reportCount;
    if (count > 10) return 'severity-high';
    if (count > 5) return 'severity-medium';
    return 'severity-low';
  });
}