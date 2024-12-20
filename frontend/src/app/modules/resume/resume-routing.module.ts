import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './pages/main/main.component';
import { ResumeDetailComponent } from './pages/resume-detail/resume-detail.component';
import { CreateResumeComponent } from './pages/create-resume/create-resume.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent
  },
  {
    path: 'id/:id',
    component: ResumeDetailComponent
  },
  {
    path: 'create',
    component: CreateResumeComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResumeRoutingModule { }
