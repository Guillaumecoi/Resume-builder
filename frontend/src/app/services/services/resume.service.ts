/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { create2 } from '../fn/resume/create-2';
import { Create2$Params } from '../fn/resume/create-2';
import { delete2 } from '../fn/resume/delete-2';
import { Delete2$Params } from '../fn/resume/delete-2';
import { get2 } from '../fn/resume/get-2';
import { Get2$Params } from '../fn/resume/get-2';
import { getAll } from '../fn/resume/get-all';
import { GetAll$Params } from '../fn/resume/get-all';
import { PageResponseResumeResponse } from '../models/page-response-resume-response';
import { postMethodName } from '../fn/resume/post-method-name';
import { PostMethodName$Params } from '../fn/resume/post-method-name';
import { ResumeDetailResponse } from '../models/resume-detail-response';
import { update2 } from '../fn/resume/update-2';
import { Update2$Params } from '../fn/resume/update-2';
import { uploadPicture } from '../fn/resume/upload-picture';
import { UploadPicture$Params } from '../fn/resume/upload-picture';

@Injectable({ providedIn: 'root' })
export class ResumeService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getAll()` */
  static readonly GetAllPath = '/resumes';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll$Response(params?: GetAll$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseResumeResponse>> {
    return getAll(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAll$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll(params?: GetAll$Params, context?: HttpContext): Observable<PageResponseResumeResponse> {
    return this.getAll$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseResumeResponse>): PageResponseResumeResponse => r.body)
    );
  }

  /** Path part for operation `create2()` */
  static readonly Create2Path = '/resumes';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create2$Response(params: Create2$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return create2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create2(params: Create2$Params, context?: HttpContext): Observable<number> {
    return this.create2$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `get2()` */
  static readonly Get2Path = '/resumes/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `get2()` instead.
   *
   * This method doesn't expect any request body.
   */
  get2$Response(params: Get2$Params, context?: HttpContext): Observable<StrictHttpResponse<ResumeDetailResponse>> {
    return get2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `get2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  get2(params: Get2$Params, context?: HttpContext): Observable<ResumeDetailResponse> {
    return this.get2$Response(params, context).pipe(
      map((r: StrictHttpResponse<ResumeDetailResponse>): ResumeDetailResponse => r.body)
    );
  }

  /** Path part for operation `update2()` */
  static readonly Update2Path = '/resumes/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update2$Response(params: Update2$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return update2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update2(params: Update2$Params, context?: HttpContext): Observable<void> {
    return this.update2$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `uploadPicture()` */
  static readonly UploadPicturePath = '/resumes/{id}/uploadPicture';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `uploadPicture()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadPicture$Response(params: UploadPicture$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return uploadPicture(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `uploadPicture$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  uploadPicture(params: UploadPicture$Params, context?: HttpContext): Observable<void> {
    return this.uploadPicture$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `delete2()` */
  static readonly Delete2Path = '/resumes/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete2()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2$Response(params: Delete2$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2(params: Delete2$Params, context?: HttpContext): Observable<void> {
    return this.delete2$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `postMethodName()` */
  static readonly PostMethodNamePath = '/resumes/deleteAll';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `postMethodName()` instead.
   *
   * This method doesn't expect any request body.
   */
  postMethodName$Response(params?: PostMethodName$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return postMethodName(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `postMethodName$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  postMethodName(params?: PostMethodName$Params, context?: HttpContext): Observable<void> {
    return this.postMethodName$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
