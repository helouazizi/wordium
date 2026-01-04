import { inject, Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { map, shareReplay } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ResponsiveService {
  private bpObs = inject(BreakpointObserver);

  readonly isHandset$: Observable<boolean> = this.bpObs
    .observe([Breakpoints.HandsetPortrait, Breakpoints.HandsetLandscape])
    .pipe(
      map((s) => s.matches),
      shareReplay(1)
    );

  readonly isTablet$: Observable<boolean> = this.bpObs
    .observe([Breakpoints.TabletPortrait, Breakpoints.TabletLandscape])
    .pipe(
      map((s) => s.matches),
      shareReplay(1)
    );

  readonly isDesktop$: Observable<boolean> = this.bpObs.observe([Breakpoints.Web]).pipe(
    map((s) => s.matches),
    shareReplay(1)
  );
}
