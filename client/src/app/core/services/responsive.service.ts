// src/app/core/services/responsive.service.ts
import { Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class ResponsiveService {
  isMobile$: Observable<boolean>;

  constructor(private breakpointObserver: BreakpointObserver) {
    this.isMobile$ = this.breakpointObserver.observe([Breakpoints.Handset]).pipe(
      map((result) => result.matches),
      shareReplay(1) // ensures all subscribers get the latest value
    );
  }
}

// usage

// constructor(private responsive: ResponsiveService) {}

// ngOnInit() {
//   this.responsive.isMobile$.subscribe(isMobile => this.isMobile = isMobile);
// }
