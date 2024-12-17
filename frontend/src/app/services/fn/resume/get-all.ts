/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseResumeResponse } from '../../models/page-response-resume-response';

export interface GetAll$Params {
  page?: number;
  size?: number;
  order?: string;
}

export function getAll(http: HttpClient, rootUrl: string, params?: GetAll$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseResumeResponse>> {
  const rb = new RequestBuilder(rootUrl, getAll.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.query('order', params.order, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseResumeResponse>;
    })
  );
}

getAll.PATH = '/resumes';
