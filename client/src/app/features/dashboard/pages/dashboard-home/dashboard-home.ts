import { Component, inject } from '@angular/core';
import { SessionService } from '../../../../core/services/session.service';
import { AsyncPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard-home',
  imports: [],
  templateUrl: './dashboard-home.html',
  styleUrl: './dashboard-home.scss',
})
export class DashboardHome {
  session = inject(SessionService);
  user = this.session.getUser();
  
}
