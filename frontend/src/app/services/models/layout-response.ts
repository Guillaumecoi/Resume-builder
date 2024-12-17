/* tslint:disable */
/* eslint-disable */
import { ColorScheme } from '../models/color-scheme';
import { ColumnResponse } from '../models/column-response';
import { LatexMethodResponse } from '../models/latex-method-response';
export interface LayoutResponse {
  colorScheme?: ColorScheme;
  columnSeparator?: number;
  columns?: Array<ColumnResponse>;
  id?: number;
  latexMethods?: Array<LatexMethodResponse>;
  numberOfColumns?: number;
  pageSize?: 'A4' | 'LETTER';
  sectionTitleMethod?: string;
}
