export interface NavItem {
  label: string;
  icon: string;
  route: string;
}

export const NAV_LINKS: NavItem[] = [
  { label: 'Feed', icon: 'home', route: '/feed' },
  { label: 'Discover', icon: 'explore', route: '/discover' },
  { label: 'write', icon: 'edit_note', route: '/write' },
];