# 🚀 Resume Builder

Easily create, manage, and export professional resumes with our user-friendly application. Enjoy secure authentication, reliable cloud storage, and effortless PDF generation.

## ✨ Key Features

- **Effortless Consistency**: Automatically maintain consistent layout, spacing, and font sizes across your resume
- **Highly Customizable Templates**: Choose from a variety of templates and easily customize every aspect to fit your unique style
- **Tech-Friendly Editing**: Advanced editing options for tech-savvy users to fine-tune their resumes
- **Seamless PDF Generation**: Generate professional-quality PDFs using LaTeX for precise formatting
- **User-Friendly Interface**: Intuitive design that makes resume creation simple and enjoyable

## 🛠️ Built With Modern Tech

- **Angular**: Powering a reactive, modern frontend
- **Spring Boot**: RESTful API with enterprise-grade reliability
- **Keycloak**: Rock-solid security and authentication
- **PostgreSQL**: Robust data persistence
- **Docker**: Containerized for seamless deployment

## 🎯 Development Approach

> 🚧 **Current Phase**: Phase 2 - Frontend Development
> 
> This project uses rapid development to quickly achieve a working proof of concept. Code quality and comprehensive testing will be addressed after core functionality is established.

### Development Phases

1. **Phase 1**: Core Backend Functionality
   > Establishing foundational backend services with security, database, and core business logic
   - ~~Spring Boot REST API setup~~
   - ~~PostgreSQL database integration~~
   - ~~Keycloak authentication setup~~
   - ~~Local development environment with Docker Compose~~
   - ~~Basic resume CRUD operations~~
   - ~~Simple PDF generation~~

2. **Phase 2**: Frontend Development *(Current)*
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