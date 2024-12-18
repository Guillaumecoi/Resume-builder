/* tslint:disable */
/* eslint-disable */
import { SectionRequest } from '../models/section-request';
export interface ResumeRequest {
  id?: number;
  sections?: Array<SectionRequest>;
  title: string;
}
