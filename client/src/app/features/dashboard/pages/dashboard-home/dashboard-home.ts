import { Component, inject } from '@angular/core';
import { SessionService } from '../../../../core/services/session.service';
import { AsyncPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard-home',
  imports: [NgIf,AsyncPipe],
  templateUrl: './dashboard-home.html',
  styleUrl: './dashboard-home.scss',
})
export class DashboardHome {
  user$ = inject(SessionService).user$;
  
}
