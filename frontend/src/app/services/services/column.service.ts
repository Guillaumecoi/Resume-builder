/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { ColumnResponse } from '../models/column-response';
import { createColumn } from '../fn/column/create-column';
import { CreateColumn$Params } from '../fn/column/create-column';
import { deleteColumn } from '../fn/column/delete-column';
import { DeleteColumn$Params } from '../fn/column/delete-column';
import { getColumn } from '../fn/column/get-column';
import { GetColumn$Params } from '../fn/column/get-column';
import { updateColumn } from '../fn/column/update-column';
import { UpdateColumn$Params } from '../fn/column/update-column';

@Injectable({ providedIn: 'root' })
export class ColumnService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createColumn()` */
  static readonly CreateColumnPath = '/columns';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createColumn()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createColumn$Response(params: CreateColumn$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createColumn(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createColumn$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createColumn(params: CreateColumn$Params, context?: HttpContext): Observable<number> {
    return this.createColumn$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getColumn()` */
  static readonly GetColumnPath = '/columns/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getColumn()` instead.
   *
   * This method doesn't expect any request body.
   */
  getColumn$Response(params: GetColumn$Params, context?: HttpContext): Observable<StrictHttpResponse<ColumnResponse>> {
    return getColumn(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getColumn$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getColumn(params: GetColumn$Params, context?: HttpContext): Observable<ColumnResponse> {
    return this.getColumn$Response(params, context).pipe(
      map((r: StrictHttpResponse<ColumnResponse>): ColumnResponse => r.body)
    );
  }

  /** Path part for operation `updateColumn()` */
  static readonly UpdateColumnPath = '/columns/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateColumn()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateColumn$Response(params: UpdateColumn$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateColumn(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateColumn$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateColumn(params: UpdateColumn$Params, context?: HttpContext): Observable<void> {
    return this.updateColumn$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteColumn()` */
  static readonly DeleteColumnPath = '/columns/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteColumn()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteColumn$Response(params: DeleteColumn$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteColumn(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteColumn$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteColumn(params: DeleteColumn$Params, context?: HttpContext): Observable<void> {
    return this.deleteColumn$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
