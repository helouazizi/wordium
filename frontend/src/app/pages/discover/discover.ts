import { Component } from '@angular/core';
import { UserList } from '../../shared/components/user-list/user-list';

@Component({
  selector: 'app-discover',
  imports: [UserList],
  templateUrl: './discover.html',
  styleUrl: './discover.scss',
})
export class Discover {

}
