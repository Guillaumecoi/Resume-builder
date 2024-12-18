/* tslint:disable */
/* eslint-disable */
import { ColumnSectionRequest } from '../models/column-section-request';
export interface ColumnRequest {
  backgroundColor?: 'PRIMARY' | 'SECONDARY' | 'ACCENT' | 'DARK_BG' | 'LIGHT_BG' | 'DARK_TEXT' | 'LIGHT_TEXT';
  borderBottom?: number;
  borderColor?: 'PRIMARY' | 'SECONDARY' | 'ACCENT' | 'DARK_BG' | 'LIGHT_BG' | 'DARK_TEXT' | 'LIGHT_TEXT';
  borderLeft?: number;
  borderRight?: number;
  borderTop?: number;
  columnNumber?: number;
  id?: number;
  layoutId?: number;
  paddingBottom?: number;
  paddingLeft?: number;
  paddingRight?: number;
  paddingTop?: number;
  sectionMappings: Array<ColumnSectionRequest>;
  textColor?: 'PRIMARY' | 'SECONDARY' | 'ACCENT' | 'DARK_BG' | 'LIGHT_BG' | 'DARK_TEXT' | 'LIGHT_TEXT';
}
