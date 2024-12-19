import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ResumeRoutingModule } from './resume-routing.module';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ThemeToggleComponent } from './components/navbar/components/theme-toggle/theme-toggle.component';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ResumeRoutingModule,
    NavbarComponent,
    ThemeToggleComponent
  ]
})
export class ResumeModule { }
