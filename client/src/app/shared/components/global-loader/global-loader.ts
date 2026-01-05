import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoaderService } from '../../../core/services/loader.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [CommonModule, NgIf],
  templateUrl: './global-loader.html',
  styleUrls: ['./global-loader.scss'],
})
export class Loading {
  // we can specify the loder  @Input() variant: 'auth' | 'feed' | 'dashboard' = 'auth';
  private loader = inject(LoaderService);
  loading$ = this.loader.loading$;
}
