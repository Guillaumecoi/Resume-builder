# Model of the resume structure
Here is a complete overview of the domain. All information about the resume is stored in the classes: Resume, Section, SubSection and SectionItem.

A resume can have multiple layouts, which define where, how, and which information is displayed. This can be handy for managing multiple versions of a resume depending on the application, while allowing changes to be reflected across all versions.

The Layout Classes Column, ColumnSection, LayoutSection, LayoutSectionRow, LayoutSubSection and LayoutSectionItem all implement LatexMethod. This is not shown to simplify the overview.

```mermaid
erDiagram
    User ||--o{ Resume : ""
    Resume ||--o{ Section : ""
    Resume ||--o{ Layout : ""
    Section ||--o{ SubSection : ""
    Section ||--o{ LayoutSection : ""
    SubSection ||--o{ SectionItem : ""
    SubSection ||--o{ LayoutSubSection : ""
    SectionItem ||--o{ LayoutSectionItem : ""
    Layout ||--|| Header : ""
    Layout ||--o{ Body : ""
    Layout ||--|| Footer : ""
    Header ||--o{ Column : ""
    Body ||--o{ Column : ""
    Footer ||--o{ Column : ""
    Column ||--o{ ColumnSection : ""
    ColumnSection ||--|| LayoutSection : ""
    ColumnSection ||--o{ LayoutSectionRow : ""
    LayoutSectionRow ||--o{ LayoutSubSection : ""
    LayoutSubSection ||--o{ LayoutSectionItem : ""
```



