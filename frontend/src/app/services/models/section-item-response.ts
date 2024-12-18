/* tslint:disable */
/* eslint-disable */
import { LatexMethodResponse } from '../models/latex-method-response';
export interface SectionItemResponse {
  alignment?: 'LEFT' | 'CENTER' | 'RIGHT' | 'JUSTIFIED';
  data?: {
[key: string]: {
};
};
  id?: number;
  itemOrder?: number;
  latexMethod?: LatexMethodResponse;
  type?: string;
}
