/* tslint:disable */
/* eslint-disable */
import { SectionItemRequest } from '../models/section-item-request';
export interface SectionRequest {
  id?: number;
  resumeId: number;
  sectionItems?: Array<SectionItemRequest>;
  showTitle?: boolean;
  title: string;
}
