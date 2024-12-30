/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { LatexMethodResponse } from '../../models/latex-method-response';

export interface GetLatexMethod$Params {
  id: number;
}

export function getLatexMethod(http: HttpClient, rootUrl: string, params: GetLatexMethod$Params, context?: HttpContext): Observable<StrictHttpResponse<LatexMethodResponse>> {
  const rb = new RequestBuilder(rootUrl, getLatexMethod.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<LatexMethodResponse>;
    })
  );
}

getLatexMethod.PATH = '/latexmethods/{id}';
