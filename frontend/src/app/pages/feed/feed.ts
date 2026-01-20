import { Component } from '@angular/core';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';

import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatFormField, MatLabel } from '@angular/material/form-field';

@Component({
  selector: 'app-feed',
  imports: [MatToolbarModule,MatSidenavModule],
  templateUrl: './feed.html',
  styleUrl: './feed.scss',
})
export class Feed {

}
