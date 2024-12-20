/* tslint:disable */
/* eslint-disable */
import { SectionItemData } from '../models/section-item-data';
export type WorkExperience = SectionItemData & {
'jobTitle'?: string;
'companyName'?: string;
'period'?: string;
'description'?: string;
'responsibilities'?: Array<string>;
} & {
};
