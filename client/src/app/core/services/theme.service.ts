// src/app/shared/services/theme.service.ts
import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private renderer: Renderer2;
  private _theme$ = new BehaviorSubject<Theme>('light');

  readonly theme$ = this._theme$.asObservable();

  constructor(rf: RendererFactory2) {
    this.renderer = rf.createRenderer(null, null);
    const saved = localStorage.getItem('app-theme') as Theme;
    if (saved) this.setTheme(saved);
  }

  setTheme(theme: Theme) {
    const old = this._theme$.value;
    this._theme$.next(theme);
    this.renderer.removeClass(document.body, `${old}-theme`);
    this.renderer.addClass(document.body, `${theme}-theme`);
    localStorage.setItem('app-theme', theme);
  }

  toggle() {
    this.setTheme(this._theme$.value === 'light' ? 'dark' : 'light');
  }
}