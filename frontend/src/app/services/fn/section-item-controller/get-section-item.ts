/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SectionItemResponse } from '../../models/section-item-response';

export interface GetSectionItem$Params {
  id: number;
}

export function getSectionItem(http: HttpClient, rootUrl: string, params: GetSectionItem$Params, context?: HttpContext): Observable<StrictHttpResponse<SectionItemResponse>> {
  const rb = new RequestBuilder(rootUrl, getSectionItem.PATH, 'get');
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

getSectionItem.PATH = '/section-items/{id}';
