/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { LatexMethodResponse } from '../../models/latex-method-response';

export interface GetLatexMethods$Params {
  id: number;
}

export function getLatexMethods(http: HttpClient, rootUrl: string, params: GetLatexMethods$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<LatexMethodResponse>>> {
  const rb = new RequestBuilder(rootUrl, getLatexMethods.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<LatexMethodResponse>>;
    })
  );
}

getLatexMethods.PATH = '/layouts/{id}/methods';
