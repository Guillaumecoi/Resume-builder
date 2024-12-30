/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createSection } from '../fn/resume-sections/create-section';
import { CreateSection$Params } from '../fn/resume-sections/create-section';
import { deleteSection } from '../fn/resume-sections/delete-section';
import { DeleteSection$Params } from '../fn/resume-sections/delete-section';
import { getSection } from '../fn/resume-sections/get-section';
import { GetSection$Params } from '../fn/resume-sections/get-section';
import { SectionResponse } from '../models/section-response';
import { updateSection } from '../fn/resume-sections/update-section';
import { UpdateSection$Params } from '../fn/resume-sections/update-section';

@Injectable({ providedIn: 'root' })
export class ResumeSectionsService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createSection()` */
  static readonly CreateSectionPath = '/sections';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createSection()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createSection$Response(params: CreateSection$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createSection$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createSection(params: CreateSection$Params, context?: HttpContext): Observable<number> {
    return this.createSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getSection()` */
  static readonly GetSectionPath = '/sections/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getSection()` instead.
   *
   * This method doesn't expect any request body.
   */
  getSection$Response(params: GetSection$Params, context?: HttpContext): Observable<StrictHttpResponse<SectionResponse>> {
    return getSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getSection$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getSection(params: GetSection$Params, context?: HttpContext): Observable<SectionResponse> {
    return this.getSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<SectionResponse>): SectionResponse => r.body)
    );
  }

  /** Path part for operation `updateSection()` */
  static readonly UpdateSectionPath = '/sections/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateSection()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateSection$Response(params: UpdateSection$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateSection$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateSection(params: UpdateSection$Params, context?: HttpContext): Observable<void> {
    return this.updateSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteSection()` */
  static readonly DeleteSectionPath = '/sections/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteSection()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteSection$Response(params: DeleteSection$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteSection$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteSection(params: DeleteSection$Params, context?: HttpContext): Observable<void> {
    return this.deleteSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
