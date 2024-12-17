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
import { create5 } from '../fn/column-section/create-5';
import { Create5$Params } from '../fn/column-section/create-5';
import { delete5 } from '../fn/column-section/delete-5';
import { Delete5$Params } from '../fn/column-section/delete-5';
import { get5 } from '../fn/column-section/get-5';
import { Get5$Params } from '../fn/column-section/get-5';
import { update5 } from '../fn/column-section/update-5';
import { Update5$Params } from '../fn/column-section/update-5';

@Injectable({ providedIn: 'root' })
export class ColumnSectionService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `create5()` */
  static readonly Create5Path = '/columnsections';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create5()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create5$Response(params: Create5$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return create5(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create5$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create5(params: Create5$Params, context?: HttpContext): Observable<number> {
    return this.create5$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `get5()` */
  static readonly Get5Path = '/columnsections/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `get5()` instead.
   *
   * This method doesn't expect any request body.
   */
  get5$Response(params: Get5$Params, context?: HttpContext): Observable<StrictHttpResponse<ColumnSectionResponse>> {
    return get5(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `get5$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  get5(params: Get5$Params, context?: HttpContext): Observable<ColumnSectionResponse> {
    return this.get5$Response(params, context).pipe(
      map((r: StrictHttpResponse<ColumnSectionResponse>): ColumnSectionResponse => r.body)
    );
  }

  /** Path part for operation `update5()` */
  static readonly Update5Path = '/columnsections/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update5()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update5$Response(params: Update5$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return update5(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update5$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update5(params: Update5$Params, context?: HttpContext): Observable<void> {
    return this.update5$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `delete5()` */
  static readonly Delete5Path = '/columnsections/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete5()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete5$Response(params: Delete5$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete5(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete5$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete5(params: Delete5$Params, context?: HttpContext): Observable<void> {
    return this.delete5$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
