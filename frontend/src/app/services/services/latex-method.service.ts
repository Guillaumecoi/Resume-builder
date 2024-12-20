/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createLatexMethod } from '../fn/latex-method/create-latex-method';
import { CreateLatexMethod$Params } from '../fn/latex-method/create-latex-method';
import { deleteLatexMethod } from '../fn/latex-method/delete-latex-method';
import { DeleteLatexMethod$Params } from '../fn/latex-method/delete-latex-method';
import { getLatexMethod } from '../fn/latex-method/get-latex-method';
import { GetLatexMethod$Params } from '../fn/latex-method/get-latex-method';
import { LatexMethodResponse } from '../models/latex-method-response';
import { updateLatexMethod } from '../fn/latex-method/update-latex-method';
import { UpdateLatexMethod$Params } from '../fn/latex-method/update-latex-method';

@Injectable({ providedIn: 'root' })
export class LatexMethodService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createLatexMethod()` */
  static readonly CreateLatexMethodPath = '/latexmethods';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createLatexMethod()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createLatexMethod$Response(params: CreateLatexMethod$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createLatexMethod(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createLatexMethod$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createLatexMethod(params: CreateLatexMethod$Params, context?: HttpContext): Observable<number> {
    return this.createLatexMethod$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getLatexMethod()` */
  static readonly GetLatexMethodPath = '/latexmethods/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getLatexMethod()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLatexMethod$Response(params: GetLatexMethod$Params, context?: HttpContext): Observable<StrictHttpResponse<LatexMethodResponse>> {
    return getLatexMethod(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getLatexMethod$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLatexMethod(params: GetLatexMethod$Params, context?: HttpContext): Observable<LatexMethodResponse> {
    return this.getLatexMethod$Response(params, context).pipe(
      map((r: StrictHttpResponse<LatexMethodResponse>): LatexMethodResponse => r.body)
    );
  }

  /** Path part for operation `updateLatexMethod()` */
  static readonly UpdateLatexMethodPath = '/latexmethods/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateLatexMethod()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateLatexMethod$Response(params: UpdateLatexMethod$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateLatexMethod(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateLatexMethod$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateLatexMethod(params: UpdateLatexMethod$Params, context?: HttpContext): Observable<void> {
    return this.updateLatexMethod$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteLatexMethod()` */
  static readonly DeleteLatexMethodPath = '/latexmethods/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteLatexMethod()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteLatexMethod$Response(params: DeleteLatexMethod$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteLatexMethod(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteLatexMethod$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteLatexMethod(params: DeleteLatexMethod$Params, context?: HttpContext): Observable<void> {
    return this.deleteLatexMethod$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
