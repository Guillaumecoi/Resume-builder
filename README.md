# 🚀 Resume Builder

Transform your professional story into a stunning resume in minutes. Built with modern tech for developers who appreciate clean code and smooth user experience.

## ✨ Key Features

- **Smart Templates**: Create professional resumes that stand out
- **Cloud-Powered**: Your data, always secure, always available
- **Responsive**: Perfect on every device
- **Export Ready**: Download your resume as PDF

## 🛠️ Built With Modern Tech

- **Angular**: Powering a reactive, modern frontend
- **Spring Boot**: RESTful API with enterprise-grade reliability
- **Keycloak**: Rock-solid security and authentication
- **PostgreSQL**: Robust data persistence
- **Docker**: Containerized for seamless deployment

## 🎯 Development Approach

> 🚧 **Current Phase**: Phase 1 - Core Backend Functionality
> 
> This project uses rapid development to quickly achieve a working proof of concept. Code quality and comprehensive testing will be addressed after core functionality is established.

### Development Phases

1. **Phase 1**: Core Backend Functionality *(Current)*
   > Establishing foundational backend services with security, database, and core business logic
   - Spring Boot REST API setup
   - ~~PostgreSQL database integration~~
   - ~~Keycloak authentication setup~~
   - ~~Local development environment with Docker Compose~~
   - Basic resume CRUD operations
   - Simple PDF generation

2. **Phase 2**: Frontend Development
   > Simple but functional GUI implementation focusing on the core features
   - ~~Keycloak authentication integration~~
   - Backend service integration
   - Resume overview page implementation
   - Resume editor page implementation

3. **Phase 3**: Cloud Infrastructure
   > Containerization and deployment of the application stack to cloud infrastructure
   - Docker containerization
   - Environment configuration
   - Cloud deployment (database, backend, frontend, Keycloak)
   - CI/CD pipeline with GitHub Actions

4. **Phase 4**: Code Quality & Testing
   > Ensuring application reliability through comprehensive testing
   - Unit tests (JUnit, Mockito)
   - Integration tests
   - API documentation (SpringDoc)
   - Performance optimization

5. **Phase 5**: Feature Expansion
   > Enhancing user experience and adding advanced resume management capabilities
   - Enhanced UI/UX design
   - Multiple resume templates
   - Template customization options
   - AI-powered content suggestions

## 🚀 Getting Started

### Prerequisites
- Docker Desktop or Docker Engine with Docker Compose

### Installation

1. Clone the repository
```bash
git clone https://github.com//resume-builder.git
cd resume-builder
```

2. Start all services using Docker Compose
```bash
docker compose build
docker compose up
```

3. Access the application
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080
- Keycloak: http://localhost:9090

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details