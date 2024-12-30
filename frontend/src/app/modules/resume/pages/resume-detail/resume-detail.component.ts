import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ResumeService } from 'src/app/services/services/resume.service';
import { ResumeDetailResponse, ResumeRequest } from 'src/app/services/models';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from 'src/app/components/navbar/navbar.component';

@Component({
  selector: 'app-resume-detail',
  standalone: true,
  imports: [FormsModule, CommonModule, NavbarComponent],
  templateUrl: './resume-detail.component.html',
  styleUrls: ['./resume-detail.component.scss']
})
export class ResumeDetailComponent implements OnInit {
  resume: ResumeDetailResponse = {
    title: '',
    id: 0,
    createdDate: '',
    lastModifiedDate: ''
  };
  loading = false;

  constructor(private route: ActivatedRoute, private resumeService: ResumeService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);
    this.loading = true;
    this.resumeService.getResume({ id: id }).subscribe(
      res => {
        this.resume = res;
        this.loading = false;
      },
      err => {
        console.error(err);
        this.loading = false;
      }
    );
  }

  updateResume(): void {
    if (!this.resume) return;
    if (!this.resume.id) return;
    if (!this.resume.title) return;

    const updateRequest: ResumeRequest = {
      id: this.resume.id,
      title: this.resume.title
    };

    this.loading = true;
    this.resumeService.updateResume({ id: this.resume.id, body: updateRequest })
        .subscribe({
            next: () => {
                console.log('Resume updated successfully');
                this.loading = false;
            },
            error: (error) => {
                console.error('Error updating resume:', error);
                this.loading = false;
                // Handle error (show message, etc.)
            }
        });
  }
}
