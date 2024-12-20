/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { createLayout } from '../fn/layout/create-layout';
import { CreateLayout$Params } from '../fn/layout/create-layout';
import { deleteLayout } from '../fn/layout/delete-layout';
import { DeleteLayout$Params } from '../fn/layout/delete-layout';
import { generateLatexPdf } from '../fn/layout/generate-latex-pdf';
import { GenerateLatexPdf$Params } from '../fn/layout/generate-latex-pdf';
import { getLatexMethods } from '../fn/layout/get-latex-methods';
import { GetLatexMethods$Params } from '../fn/layout/get-latex-methods';
import { getLayout } from '../fn/layout/get-layout';
import { GetLayout$Params } from '../fn/layout/get-layout';
import { LatexMethodResponse } from '../models/latex-method-response';
import { LayoutResponse } from '../models/layout-response';
import { updateLayout } from '../fn/layout/update-layout';
import { UpdateLayout$Params } from '../fn/layout/update-layout';

@Injectable({ providedIn: 'root' })
export class LayoutService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createLayout()` */
  static readonly CreateLayoutPath = '/layouts';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createLayout()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createLayout$Response(params: CreateLayout$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return createLayout(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createLayout$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createLayout(params: CreateLayout$Params, context?: HttpContext): Observable<number> {
    return this.createLayout$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getLayout()` */
  static readonly GetLayoutPath = '/layouts/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getLayout()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLayout$Response(params: GetLayout$Params, context?: HttpContext): Observable<StrictHttpResponse<LayoutResponse>> {
    return getLayout(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getLayout$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLayout(params: GetLayout$Params, context?: HttpContext): Observable<LayoutResponse> {
    return this.getLayout$Response(params, context).pipe(
      map((r: StrictHttpResponse<LayoutResponse>): LayoutResponse => r.body)
    );
  }

  /** Path part for operation `updateLayout()` */
  static readonly UpdateLayoutPath = '/layouts/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateLayout()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateLayout$Response(params: UpdateLayout$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return updateLayout(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateLayout$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateLayout(params: UpdateLayout$Params, context?: HttpContext): Observable<void> {
    return this.updateLayout$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `deleteLayout()` */
  static readonly DeleteLayoutPath = '/layouts/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteLayout()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteLayout$Response(params: DeleteLayout$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteLayout(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteLayout$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteLayout(params: DeleteLayout$Params, context?: HttpContext): Observable<void> {
    return this.deleteLayout$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `generateLatexPdf()` */
  static readonly GenerateLatexPdfPath = '/layouts/{id}/pdf';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `generateLatexPdf()` instead.
   *
   * This method doesn't expect any request body.
   */
  generateLatexPdf$Response(params: GenerateLatexPdf$Params, context?: HttpContext): Observable<StrictHttpResponse<Blob>> {
    return generateLatexPdf(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `generateLatexPdf$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  generateLatexPdf(params: GenerateLatexPdf$Params, context?: HttpContext): Observable<Blob> {
    return this.generateLatexPdf$Response(params, context).pipe(
      map((r: StrictHttpResponse<Blob>): Blob => r.body)
    );
  }

  /** Path part for operation `getLatexMethods()` */
  static readonly GetLatexMethodsPath = '/layouts/{id}/methods';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getLatexMethods()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLatexMethods$Response(params: GetLatexMethods$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<LatexMethodResponse>>> {
    return getLatexMethods(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getLatexMethods$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getLatexMethods(params: GetLatexMethods$Params, context?: HttpContext): Observable<Array<LatexMethodResponse>> {
    return this.getLatexMethods$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<LatexMethodResponse>>): Array<LatexMethodResponse> => r.body)
    );
  }

}
