import { Injectable, signal, effect } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  isDark = signal<boolean>(localStorage.getItem('theme') === 'dark');

  constructor() {
    effect(() => {
      const mode = this.isDark() ? 'dark' : 'light';
      localStorage.setItem('theme', mode);

      if (this.isDark()) {
        document.documentElement.classList.add('dark-mode');
      } else {
        document.documentElement.classList.remove('dark-mode');
      }
    });
  }

  toggle() {
    this.isDark.update((v) => !v);
  }
}
