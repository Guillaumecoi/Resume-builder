import { Component } from '@angular/core';
import { ThemeToggleComponent } from './components/theme-toggle/theme-toggle.component';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [ThemeToggleComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  constructor(private keycloakService: KeycloakService) {}

  logout() {
    this.keycloakService.logout();
  }

  accountManagement() {
    this.keycloakService.accountManagement();
  }
}