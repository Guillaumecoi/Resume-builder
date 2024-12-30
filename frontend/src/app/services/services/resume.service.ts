/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createResume } from '../fn/resume/create-resume';
import { CreateResume$Params } from '../fn/resume/create-resume';
import { deleteAllResumes } from '../fn/resume/delete-all-resumes';
import { DeleteAllResumes$Params } from '../fn/resume/delete-all-resumes';
import { deleteResume } from '../fn/resume/delete-resume';
import { DeleteResume$Params } from '../fn/resume/delete-resume';
import { getAllResumes } from '../fn/resume/get-all-resumes';
import { GetAllResumes$Params } from '../fn/resume/get-all-resumes';
import { getResume } from '../fn/resume/get-resume';
import { GetResume$Params } from '../fn/resume/get-resume';
import { PageResponseResumeResponse } from '../models/page-response-resume-response';
import { ResumeDetailResponse } from '../models/resume-detail-response';
import { updateResume } from '../fn/resume/update-resume';
import { UpdateResume$Params } from '../fn/resume/update-resume';
import { uploadResumePicture } from '../fn/resume/upload-resume-picture';
import { UploadResumePicture$Params } from '../fn/resume/upload-resume-picture';

@Injectable({ providedIn: 'root' })
export class ResumeService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getAllResumes()` */
  static readonly GetAllResumesPath = '/resumes';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllResumes()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllResumes$Response(params?: GetAllResumes$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseResumeResponse>> {
    return getAllResumes(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllResumes$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllResumes(params?: GetAllResumes$Params, context?: HttpContext): Observable<PageResponseResumeResponse> {
    return this.getAllResumes$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseResumeResponse>): PageResponseResumeResponse => r.body)
    );
  }

  /** Path part for operation `createResume()` */
  static readonly CreateResumePath = '/resumes';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createResume()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createResume$Response(params: CreateResume$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createResume(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createResume$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createResume(params: CreateResume$Params, context?: HttpContext): Observable<number> {
    return this.createResume$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getResume()` */
  static readonly GetResumePath = '/resumes/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getResume()` instead.
   *
   * This method doesn't expect any request body.
   */
  getResume$Response(params: GetResume$Params, context?: HttpContext): Observable<StrictHttpResponse<ResumeDetailResponse>> {
    return getResume(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getResume$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getResume(params: GetResume$Params, context?: HttpContext): Observable<ResumeDetailResponse> {
    return this.getResume$Response(params, context).pipe(
      map((r: StrictHttpResponse<ResumeDetailResponse>): ResumeDetailResponse => r.body)
    );
  }

  /** Path part for operation `updateResume()` */
  static readonly UpdateResumePath = '/resumes/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateResume()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateResume$Response(params: UpdateResume$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateResume(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateResume$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateResume(params: UpdateResume$Params, context?: HttpContext): Observable<void> {
    return this.updateResume$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `uploadResumePicture()` */
  static readonly UploadResumePicturePath = '/resumes/{id}/uploadPicture';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `uploadResumePicture()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadResumePicture$Response(params: UploadResumePicture$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return uploadResumePicture(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `uploadResumePicture$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadResumePicture(params: UploadResumePicture$Params, context?: HttpContext): Observable<void> {
    return this.uploadResumePicture$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteResume()` */
  static readonly DeleteResumePath = '/resumes/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteResume()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteResume$Response(params: DeleteResume$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteResume(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteResume$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteResume(params: DeleteResume$Params, context?: HttpContext): Observable<void> {
    return this.deleteResume$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteAllResumes()` */
  static readonly DeleteAllResumesPath = '/resumes/deleteAll';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteAllResumes()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteAllResumes$Response(params?: DeleteAllResumes$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteAllResumes(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteAllResumes$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteAllResumes(params?: DeleteAllResumes$Params, context?: HttpContext): Observable<void> {
    return this.deleteAllResumes$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
