/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createSectionItem$FormData } from '../fn/section-item-controller/create-section-item-form-data';
import { CreateSectionItem$FormData$Params } from '../fn/section-item-controller/create-section-item-form-data';
import { createSectionItem$Json } from '../fn/section-item-controller/create-section-item-json';
import { CreateSectionItem$Json$Params } from '../fn/section-item-controller/create-section-item-json';
import { deleteSectionItem } from '../fn/section-item-controller/delete-section-item';
import { DeleteSectionItem$Params } from '../fn/section-item-controller/delete-section-item';
import { getSectionItem } from '../fn/section-item-controller/get-section-item';
import { GetSectionItem$Params } from '../fn/section-item-controller/get-section-item';
import { SectionItemResponse } from '../models/section-item-response';
import { updateSectionItem } from '../fn/section-item-controller/update-section-item';
import { UpdateSectionItem$Params } from '../fn/section-item-controller/update-section-item';

@Injectable({ providedIn: 'root' })
export class SectionItemControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createSectionItem()` */
  static readonly CreateSectionItemPath = '/section-items';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createSectionItem$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createSectionItem$Json$Response(params: CreateSectionItem$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createSectionItem$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createSectionItem$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createSectionItem$Json(params: CreateSectionItem$Json$Params, context?: HttpContext): Observable<number> {
    return this.createSectionItem$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createSectionItem$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  createSectionItem$FormData$Response(params: CreateSectionItem$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createSectionItem$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createSectionItem$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  createSectionItem$FormData(params: CreateSectionItem$FormData$Params, context?: HttpContext): Observable<number> {
    return this.createSectionItem$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getSectionItem()` */
  static readonly GetSectionItemPath = '/section-items/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getSectionItem()` instead.
   *
   * This method doesn't expect any request body.
   */
  getSectionItem$Response(params: GetSectionItem$Params, context?: HttpContext): Observable<StrictHttpResponse<SectionItemResponse>> {
    return getSectionItem(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getSectionItem$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getSectionItem(params: GetSectionItem$Params, context?: HttpContext): Observable<SectionItemResponse> {
    return this.getSectionItem$Response(params, context).pipe(
      map((r: StrictHttpResponse<SectionItemResponse>): SectionItemResponse => r.body)
    );
  }

  /** Path part for operation `updateSectionItem()` */
  static readonly UpdateSectionItemPath = '/section-items/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateSectionItem()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateSectionItem$Response(params: UpdateSectionItem$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateSectionItem(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateSectionItem$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateSectionItem(params: UpdateSectionItem$Params, context?: HttpContext): Observable<void> {
    return this.updateSectionItem$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteSectionItem()` */
  static readonly DeleteSectionItemPath = '/section-items/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteSectionItem()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteSectionItem$Response(params: DeleteSectionItem$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteSectionItem(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteSectionItem$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteSectionItem(params: DeleteSectionItem$Params, context?: HttpContext): Observable<void> {
    return this.deleteSectionItem$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
