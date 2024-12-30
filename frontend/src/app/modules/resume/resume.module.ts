import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ResumeRoutingModule } from './resume-routing.module';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { ThemeToggleComponent } from '../../components/navbar/components/theme-toggle/theme-toggle.component';
import { MainComponent } from './pages/main/main.component';
import { ResumeDetailComponent } from './pages/resume-detail/resume-detail.component';
import { CreateResumeComponent } from './pages/create-resume/create-resume.component';


@NgModule({
  declarations: [
  ],
  imports: [
    MainComponent,
    ResumeDetailComponent,
    CreateResumeComponent,
    CommonModule,
    ResumeRoutingModule,
    NavbarComponent,
    ThemeToggleComponent
  ]
})
export class ResumeModule { }
