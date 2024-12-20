/* tslint:disable */
/* eslint-disable */
import { ColorScheme } from '../models/color-scheme';
import { ColumnRequest } from '../models/column-request';
import { LatexMethodRequest } from '../models/latex-method-request';
export interface LayoutRequest {
  colorScheme: ColorScheme;
  columnSeparator: number;
  columns?: Array<ColumnRequest>;
  id?: number;
  latexMethods: Array<LatexMethodRequest>;
  numberOfColumns: number;
  pageSize?: 'A4' | 'A5' | 'LETTER' | 'B5' | 'EXECUTIVE';
  resumeId: number;
  sectionTitleMethod?: string;
}
