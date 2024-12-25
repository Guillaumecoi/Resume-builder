# Model of the resume structure
Here is a complete overview of the domain. All information about the resume is stored in the classes: Resume, Section, and SectionItem.

A resume can have multiple layouts, which define where, how, and which information is displayed. This can be handy for managing multiple versions of a resume depending on the application, while allowing changes to be reflected across all versions.

```mermaid
erDiagram
    User ||--o{ Resume : ""
    Resume ||--o{ Section : ""
    Section ||--o{ SectionItem : ""
    Resume ||--o{ Layout : ""
    Layout ||--o{ Column : ""
    Layout ||--o{ LatexMethod : ""
    Column ||--o{ ColumnSection : ""
    Section ||--o{ ColumnSection : ""
    LatexMethod ||--o{ ColumnSection : ""
    SectionItem ||--o{ LayoutSectionItem : ""
    LatexMethod ||--o{ LayoutSectionItem : ""
    ColumnSection ||--o{ LayoutSectionItem : ""
```

## Resume structure
It is here where the information of the resume is stored
```mermaid
erDiagram
Resume{
    String title
}

Section {
    String title
    String icon
    boolean showTitle
}

SectionItem {
    SectionItemData item
    Integer itemOrder
}

Resume ||--o{ Section : ""
Section ||--o{ SectionItem : ""
```



