/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { create3 } from '../fn/layout/create-3';
import { Create3$Params } from '../fn/layout/create-3';
import { delete3 } from '../fn/layout/delete-3';
import { Delete3$Params } from '../fn/layout/delete-3';
import { generateLatexPdf } from '../fn/layout/generate-latex-pdf';
import { GenerateLatexPdf$Params } from '../fn/layout/generate-latex-pdf';
import { get3 } from '../fn/layout/get-3';
import { Get3$Params } from '../fn/layout/get-3';
import { getLatexMethods } from '../fn/layout/get-latex-methods';
import { GetLatexMethods$Params } from '../fn/layout/get-latex-methods';
import { LatexMethodResponse } from '../models/latex-method-response';
import { LayoutResponse } from '../models/layout-response';
import { update3 } from '../fn/layout/update-3';
import { Update3$Params } from '../fn/layout/update-3';

@Injectable({ providedIn: 'root' })
export class LayoutService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `create3()` */
  static readonly Create3Path = '/layouts';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create3()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create3$Response(params: Create3$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return create3(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create3$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create3(params: Create3$Params, context?: HttpContext): Observable<number> {
    return this.create3$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `get3()` */
  static readonly Get3Path = '/layouts/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `get3()` instead.
   *
   * This method doesn't expect any request body.
   */
  get3$Response(params: Get3$Params, context?: HttpContext): Observable<StrictHttpResponse<LayoutResponse>> {
    return get3(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `get3$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  get3(params: Get3$Params, context?: HttpContext): Observable<LayoutResponse> {
    return this.get3$Response(params, context).pipe(
      map((r: StrictHttpResponse<LayoutResponse>): LayoutResponse => r.body)
    );
  }

  /** Path part for operation `update3()` */
  static readonly Update3Path = '/layouts/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update3()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update3$Response(params: Update3$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return update3(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update3$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update3(params: Update3$Params, context?: HttpContext): Observable<void> {
    return this.update3$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `delete3()` */
  static readonly Delete3Path = '/layouts/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete3()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete3$Response(params: Delete3$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete3(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete3$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete3(params: Delete3$Params, context?: HttpContext): Observable<void> {
    return this.delete3$Response(params, context).pipe(
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
