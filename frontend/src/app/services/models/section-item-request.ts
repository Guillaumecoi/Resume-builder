/* tslint:disable */
/* eslint-disable */
import { Contact } from '../models/contact';
import { Education } from '../models/education';
import { Picture } from '../models/picture';
import { Skill } from '../models/skill';
import { Skillboxes } from '../models/skillboxes';
import { Textbox } from '../models/textbox';
import { Title } from '../models/title';
import { WorkExperience } from '../models/work-experience';
export interface SectionItemRequest {
  alignment?: 'LEFT' | 'CENTER' | 'RIGHT' | 'JUSTIFIED';
  id?: number;
  item: (Contact | Education | Picture | Skill | Skillboxes | Textbox | Title | WorkExperience);
  itemOrder?: number;
  latexMethodId: number;
  sectionId: number;
}
