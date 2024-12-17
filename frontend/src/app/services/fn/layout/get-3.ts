/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { LayoutResponse } from '../../models/layout-response';

export interface Get3$Params {
  id: number;
}

export function get3(http: HttpClient, rootUrl: string, params: Get3$Params, context?: HttpContext): Observable<StrictHttpResponse<LayoutResponse>> {
  const rb = new RequestBuilder(rootUrl, get3.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<LayoutResponse>;
    })
  );
}

get3.PATH = '/layouts/{id}';
