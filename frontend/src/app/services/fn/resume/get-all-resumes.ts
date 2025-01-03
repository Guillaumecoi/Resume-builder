/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseResumeResponse } from '../../models/page-response-resume-response';

export interface GetAllResumes$Params {
  page?: number;
  size?: number;
  order?: string;
}

export function getAllResumes(http: HttpClient, rootUrl: string, params?: GetAllResumes$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseResumeResponse>> {
  const rb = new RequestBuilder(rootUrl, getAllResumes.PATH, 'get');
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

getAllResumes.PATH = '/resumes';
