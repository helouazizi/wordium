import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SessionService } from './core/services/session.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('client');
  private session = inject(SessionService);

  ngOnInit() {
    console.log(localStorage.getItem('token'))
    if (localStorage.getItem('token')) {
      this.session.loadUser().subscribe();
    }
  }
}
