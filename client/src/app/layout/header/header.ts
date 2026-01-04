import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  private theme = 'light';
  isDark = () => this.theme === 'dark';

  toggleTheme() {
    this.theme = this.theme === 'light' ? 'dark' : 'light';
    // In real app: call ThemeService.toggle()
    document.documentElement.setAttribute('data-bs-theme', this.theme);
  }
  logout() {
    console.log('Logout');
  }
  openNotifications() {
    console.log('Open notifications panel');
  }
}
