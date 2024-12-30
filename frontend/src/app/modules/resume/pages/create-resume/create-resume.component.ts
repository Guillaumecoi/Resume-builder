import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ResumeRequest } from 'src/app/services/models';
import { ResumeService } from 'src/app/services/services/resume.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from 'src/app/components/navbar/navbar.component';

@Component({
  selector: 'app-create-resume',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './create-resume.component.html',
  styleUrl: './create-resume.component.scss'
})
export class CreateResumeComponent {
  resume: ResumeRequest = { title: '', sections: [] };

  constructor(private resumeService: ResumeService, private router: Router) {}

  createResume(): void {
    this.resumeService.createResume({ body: this.resume }).subscribe({
      next: (response) => {
        this.router.navigate(['/resumes', response]);
      },
      error: (error) => {
        console.error('Error creating resume:', error);
      }
    });
  }
}
