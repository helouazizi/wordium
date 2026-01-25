export interface NavItem {
  label: string;
  icon: string;
  route: string;
}

export const NAV_LINKS: NavItem[] = [
  { label: 'Feed', icon: 'home', route: '/feed' },
  { label: 'Explore', icon: 'explore', route: '/explore' },
  { label: 'write', icon: 'edit_note', route: '/write' },
  // { label: 'Profile', icon: 'person', route: '/profile' },
];