/* tslint:disable */
/* eslint-disable */
export interface LatexMethodRequest {
  id?: number;
  layoutId?: number;
  method: string;
  name: string;
  type: 'TITLE' | 'WORK_EXPERIENCE' | 'TEXTBOX' | 'SKILL' | 'EDUCATION' | 'SKILL_BOXES' | 'PICTURE' | 'CONTACT';
}
