/* tslint:disable */
/* eslint-disable */
import { SectionItemResponse } from '../models/section-item-response';
export interface SectionResponse {
  id: number;
  sectionItems: Array<SectionItemResponse>;
  showTitle: boolean;
  title: string;
}
