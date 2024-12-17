/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ColumnSectionResponse } from '../../models/column-section-response';

export interface Get5$Params {
  id: number;
}

export function get5(http: HttpClient, rootUrl: string, params: Get5$Params, context?: HttpContext): Observable<StrictHttpResponse<ColumnSectionResponse>> {
  const rb = new RequestBuilder(rootUrl, get5.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ColumnSectionResponse>;
    })
  );
}

get5.PATH = '/columnsections/{id}';
