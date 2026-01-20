import { InjectionToken } from "@angular/core";
import { environment } from "../../../environments/environment";

export interface ApiConfig {
  authBaseUrl: string;
  usersBaseUrl: string;
  postsBaseUrl: string;
}

export const API_CONFIG = new InjectionToken<ApiConfig>('API_CONFIG', {
  providedIn: 'root',
  factory: () => ({
    authBaseUrl: environment.apiAuth,
    usersBaseUrl: environment.apiUsers,
    postsBaseUrl: environment.apiPosts,
  }),
});