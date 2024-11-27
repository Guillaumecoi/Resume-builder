from entity import Entity


entity = Entity("TextBox", parent="Section")
entity.add_field("text", "String")

entity.save()
