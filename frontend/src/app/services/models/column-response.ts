/* tslint:disable */
/* eslint-disable */
import { ColumnSectionResponse } from '../models/column-section-response';
export interface ColumnResponse {
  backgroundColor?: 'PRIMARY' | 'SECONDARY' | 'ACCENT' | 'DARK_BG' | 'LIGHT_BG' | 'DARK_TEXT' | 'LIGHT_TEXT';
  borderBottom?: number;
  borderColor?: 'PRIMARY' | 'SECONDARY' | 'ACCENT' | 'DARK_BG' | 'LIGHT_BG' | 'DARK_TEXT' | 'LIGHT_TEXT';
  borderLeft?: number;
  borderRight?: number;
  borderTop?: number;
  columnNumber?: number;
  id?: number;
  paddingBottom?: number;
  paddingLeft?: number;
  paddingRight?: number;
  paddingTop?: number;
  sectionMappings?: Array<ColumnSectionResponse>;
  textColor?: 'PRIMARY' | 'SECONDARY' | 'ACCENT' | 'DARK_BG' | 'LIGHT_BG' | 'DARK_TEXT' | 'LIGHT_TEXT';
}
