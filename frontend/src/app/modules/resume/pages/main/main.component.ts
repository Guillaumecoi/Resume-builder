import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../../../components/navbar/navbar.component';
import { Router } from '@angular/router';
import { ResumeService } from 'src/app/services/services/resume.service';
import { ResumeResponse, PageResponseResumeResponse } from 'src/app/services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-main',
  standalone: true,
  imports: [NavbarComponent, CommonModule, FormsModule],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements OnInit {
  pageResponse: PageResponseResumeResponse | undefined;

  constructor(private resumeService: ResumeService, private router: Router) {}

  ngOnInit(): void {
    this.resumeService.getAllResumes().subscribe((resumes) => {
      this.pageResponse = resumes;
    });
  }

  goToDetail(id: number): void {
    this.router.navigate(['/resumes/id', id]);
  }

  createResume(): void {
    this.router.navigate(['/resumes/create']);
  }
}
