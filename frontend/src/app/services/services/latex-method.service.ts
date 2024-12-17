/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { create4 } from '../fn/latex-method/create-4';
import { Create4$Params } from '../fn/latex-method/create-4';
import { delete4 } from '../fn/latex-method/delete-4';
import { Delete4$Params } from '../fn/latex-method/delete-4';
import { get4 } from '../fn/latex-method/get-4';
import { Get4$Params } from '../fn/latex-method/get-4';
import { LatexMethodResponse } from '../models/latex-method-response';
import { update4 } from '../fn/latex-method/update-4';
import { Update4$Params } from '../fn/latex-method/update-4';

@Injectable({ providedIn: 'root' })
export class LatexMethodService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `create4()` */
  static readonly Create4Path = '/latexmethods';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create4()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create4$Response(params: Create4$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return create4(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create4$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create4(params: Create4$Params, context?: HttpContext): Observable<number> {
    return this.create4$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `get4()` */
  static readonly Get4Path = '/latexmethods/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `get4()` instead.
   *
   * This method doesn't expect any request body.
   */
  get4$Response(params: Get4$Params, context?: HttpContext): Observable<StrictHttpResponse<LatexMethodResponse>> {
    return get4(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `get4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  get4(params: Get4$Params, context?: HttpContext): Observable<LatexMethodResponse> {
    return this.get4$Response(params, context).pipe(
      map((r: StrictHttpResponse<LatexMethodResponse>): LatexMethodResponse => r.body)
    );
  }

  /** Path part for operation `update4()` */
  static readonly Update4Path = '/latexmethods/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update4()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update4$Response(params: Update4$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return update4(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update4$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update4(params: Update4$Params, context?: HttpContext): Observable<void> {
    return this.update4$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `delete4()` */
  static readonly Delete4Path = '/latexmethods/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete4()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete4$Response(params: Delete4$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete4(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete4(params: Delete4$Params, context?: HttpContext): Observable<void> {
    return this.delete4$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
