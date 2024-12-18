/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SectionItemResponse } from '../../models/section-item-response';

export interface Get1$Params {
  id: number;
}

export function get1(http: HttpClient, rootUrl: string, params: Get1$Params, context?: HttpContext): Observable<StrictHttpResponse<SectionItemResponse>> {
  const rb = new RequestBuilder(rootUrl, get1.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SectionItemResponse>;
    })
  );
}

get1.PATH = '/section-items/{id}';
