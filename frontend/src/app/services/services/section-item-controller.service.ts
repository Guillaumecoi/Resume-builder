/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { create1$FormData } from '../fn/section-item-controller/create-1-form-data';
import { Create1$FormData$Params } from '../fn/section-item-controller/create-1-form-data';
import { create1$Json } from '../fn/section-item-controller/create-1-json';
import { Create1$Json$Params } from '../fn/section-item-controller/create-1-json';
import { delete1 } from '../fn/section-item-controller/delete-1';
import { Delete1$Params } from '../fn/section-item-controller/delete-1';
import { get1 } from '../fn/section-item-controller/get-1';
import { Get1$Params } from '../fn/section-item-controller/get-1';
import { SectionItemResponse } from '../models/section-item-response';
import { update1 } from '../fn/section-item-controller/update-1';
import { Update1$Params } from '../fn/section-item-controller/update-1';

@Injectable({ providedIn: 'root' })
export class SectionItemControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `create1()` */
  static readonly Create1Path = '/section-items';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create1$Json()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create1$Json$Response(params: Create1$Json$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return create1$Json(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create1$Json$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create1$Json(params: Create1$Json$Params, context?: HttpContext): Observable<number> {
    return this.create1$Json$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create1$FormData()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  create1$FormData$Response(params: Create1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return create1$FormData(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create1$FormData$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  create1$FormData(params: Create1$FormData$Params, context?: HttpContext): Observable<number> {
    return this.create1$FormData$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `get1()` */
  static readonly Get1Path = '/section-items/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `get1()` instead.
   *
   * This method doesn't expect any request body.
   */
  get1$Response(params: Get1$Params, context?: HttpContext): Observable<StrictHttpResponse<SectionItemResponse>> {
    return get1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `get1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  get1(params: Get1$Params, context?: HttpContext): Observable<SectionItemResponse> {
    return this.get1$Response(params, context).pipe(
      map((r: StrictHttpResponse<SectionItemResponse>): SectionItemResponse => r.body)
    );
  }

  /** Path part for operation `update1()` */
  static readonly Update1Path = '/section-items/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update1$Response(params: Update1$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return update1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update1(params: Update1$Params, context?: HttpContext): Observable<void> {
    return this.update1$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `delete1()` */
  static readonly Delete1Path = '/section-items/{id}/delete';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete1()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1$Response(params: Delete1$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1(params: Delete1$Params, context?: HttpContext): Observable<void> {
    return this.delete1$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
