/* tslint:disable */
/* eslint-disable */
import { SectionResponse } from '../models/section-response';
export interface ResumeDetailResponse {
  createdDate?: string;
  id?: number;
  lastModifiedDate?: string;
  picture?: string;
  sections?: Array<SectionResponse>;
  title?: string;
}
