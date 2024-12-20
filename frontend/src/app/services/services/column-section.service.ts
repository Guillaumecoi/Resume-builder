/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { ColumnSectionResponse } from '../models/column-section-response';
import { createColumnSection } from '../fn/column-section/create-column-section';
import { CreateColumnSection$Params } from '../fn/column-section/create-column-section';
import { deleteColumnSection } from '../fn/column-section/delete-column-section';
import { DeleteColumnSection$Params } from '../fn/column-section/delete-column-section';
import { getColumnSection } from '../fn/column-section/get-column-section';
import { GetColumnSection$Params } from '../fn/column-section/get-column-section';
import { updateColumnSection } from '../fn/column-section/update-column-section';
import { UpdateColumnSection$Params } from '../fn/column-section/update-column-section';

@Injectable({ providedIn: 'root' })
export class ColumnSectionService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createColumnSection()` */
  static readonly CreateColumnSectionPath = '/columnsections';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createColumnSection()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createColumnSection$Response(params: CreateColumnSection$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createColumnSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createColumnSection$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createColumnSection(params: CreateColumnSection$Params, context?: HttpContext): Observable<number> {
    return this.createColumnSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getColumnSection()` */
  static readonly GetColumnSectionPath = '/columnsections/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getColumnSection()` instead.
   *
   * This method doesn't expect any request body.
   */
  getColumnSection$Response(params: GetColumnSection$Params, context?: HttpContext): Observable<StrictHttpResponse<ColumnSectionResponse>> {
    return getColumnSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getColumnSection$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getColumnSection(params: GetColumnSection$Params, context?: HttpContext): Observable<ColumnSectionResponse> {
    return this.getColumnSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<ColumnSectionResponse>): ColumnSectionResponse => r.body)
    );
  }

  /** Path part for operation `updateColumnSection()` */
  static readonly UpdateColumnSectionPath = '/columnsections/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateColumnSection()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateColumnSection$Response(params: UpdateColumnSection$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateColumnSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateColumnSection$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateColumnSection(params: UpdateColumnSection$Params, context?: HttpContext): Observable<void> {
    return this.updateColumnSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteColumnSection()` */
  static readonly DeleteColumnSectionPath = '/columnsections/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteColumnSection()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteColumnSection$Response(params: DeleteColumnSection$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteColumnSection(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteColumnSection$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteColumnSection(params: DeleteColumnSection$Params, context?: HttpContext): Observable<void> {
    return this.deleteColumnSection$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
