/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';


export interface Create1$FormData$Params {
  request: string;
      body: {
'file': Blob;
}
}

export function create1$FormData(http: HttpClient, rootUrl: string, params: Create1$FormData$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
  const rb = new RequestBuilder(rootUrl, create1$FormData.PATH, 'post');
  if (params) {
    rb.query('request', params.request, {});
    rb.body(params.body, 'multipart/form-data');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return (r as HttpResponse<any>).clone({ body: parseFloat(String((r as HttpResponse<any>).body)) }) as StrictHttpResponse<number>;
    })
  );
}

create1$FormData.PATH = '/section-items';
