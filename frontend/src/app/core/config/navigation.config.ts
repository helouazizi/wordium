export interface NavItem {
  label: string;
  icon: string;
  route: string;
}

export const NAV_LINKS: NavItem[] = [
  { label: 'Feed', icon: 'home', route: '/feed' },
  { label: 'Explore', icon: 'explore', route: '/explore' },
  { label: 'Notifications', icon: 'notifications', route: '/notifications' },
  { label: 'Profile', icon: 'person', route: '/profile' },
];