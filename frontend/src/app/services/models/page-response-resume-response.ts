/* tslint:disable */
/* eslint-disable */
import { ResumeResponse } from '../models/resume-response';
export interface PageResponseResumeResponse {
  content?: Array<ResumeResponse>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
