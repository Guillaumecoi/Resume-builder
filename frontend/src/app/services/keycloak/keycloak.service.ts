import {Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';
import {UserProfile} from './user-profile';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090',
        realm: 'resume-builder',
        clientId: 'rb'
      });
    }
    return this._keycloak;
  }

  private _profile: UserProfile | undefined;

  get profile(): UserProfile | undefined {
    return this._profile;
  }

  async init() {
    const authenticated = await this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false, // Optional: Disables iframe checks for performance
    });

    if (authenticated) {
      this._profile = (await this.keycloak.loadUserProfile()) as UserProfile;
      this._profile.token = this.keycloak.token || '';
      this.scheduleTokenRefresh();
    }
  }

  login() {
    return this.keycloak.login();
  }

  logout() {
    return this.keycloak.logout({ redirectUri: 'http://localhost:4200' });
  }

  accountManagement() {
    this.keycloak.accountManagement();
  }

  private scheduleTokenRefresh() {
    const refreshInterval = (this.keycloak.tokenParsed?.exp || 0) * 1000 - Date.now() - 60000; // 1 minute before expiry
    if (refreshInterval > 0) {
      setTimeout(async () => {
        try {
          await this.keycloak.updateToken(30); // Refresh if token expires in 30 seconds
          this.scheduleTokenRefresh();
        } catch (err) {
          console.error('Failed to refresh token', err);
          this.login();
        }
      }, refreshInterval);
    }
  }
}

