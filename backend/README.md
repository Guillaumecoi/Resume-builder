# Resume Builder Backend

## Code Generation Scripts

The project includes a Python-based code generation utility located at `backend/scripts/generate_entities.py` that helps create boilerplate code for new entities in the Spring Boot backend.

### Features
- Generates complete entity structure including:
  - Entity class with JPA annotations
  - DTO classes (Request/Response)
  - Repository interface
  - Service class with CRUD operations
  - Controller with REST endpoints
  - Mapper for DTO conversions
  
### Usage
Run the Python script with entity specifications to automatically generate all required boilerplate code following the project's architectural patterns.

This tool helps maintain consistency across the codebase and speeds up development by automating repetitive code creation.