import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SessionService } from './core/services/session.service';
import { ToastContainer } from './shared/components/toast-container/toast-container';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ToastContainer],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('client');
  private session = inject(SessionService);
  ngOnInit() {
    if (localStorage.getItem('token')) {
      this.session.loadUser().subscribe();
    }
  }
}
