# Clinic API

Backend API for clinic and appointment management built with Java and Spring Boot.

---

## Project Goal

This project is part of a professional backend portfolio focused on production-style architecture, clean layering, security, validation, exception handling, pagination, and scalable design.

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- H2 Database
- Maven
- Spring Security
- OpenAPI / Swagger
- Layered Architecture

---

## Architecture

The project follows a clean layered architecture:
```
Controller → Service → Repository
```

### Package Structure
```plaintext
com.clinicapi
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── mapper
├── repository
├── security
├── service
│   └── impl
└── validation
```

---

## Features Implemented

- Base project structure
- Environment configuration using YAML profiles
- Global exception handling
- Pagination response standardization
- Base entity with auditing (`createdAt` / `updatedAt`)
- User and Role domain modeling
- Repository layer with Spring Data JPA
- DTO pattern (request/response separation)
- Mapper layer for entity conversion
- Input validation with `@Valid`

---

## Work in Progress

The following features are currently being implemented:

- JWT Authentication
- Spring Security configuration
- Authentication endpoints (register / login)
- Authorization (role-based access)

---

## Authentication

The API uses JWT (JSON Web Token) for authentication.

#### Flow:

1. Register a new user:
   `POST /api/v1/auth/register`

2. Login:
   `POST /api/v1/auth/login`

3. Use the returned token in the `Authorization` header:
   `Authorization: Bearer <your_token>`

#### Example Login Response
```json
{
  "userId": 1,
  "fullName": "Nicholas Focke",
  "email": "nicholas@example.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## Environment Profiles

The application uses multiple configuration profiles:

| Profile | File | Purpose |
|---|---|---|
| Default | `application.yml` | Base configuration |
| Dev | `application-dev.yml` | Development (PostgreSQL) |
| Test | `application-test.yml` | Testing (H2 in-memory) |

---

## Database

- **PostgreSQL** is used as the main database for development
- **H2** is used for testing to enable fast, isolated test execution

---

## Running the Application

### Requirements

- Java 21+
- Maven
- PostgreSQL running locally

### Steps
```bash
# Clone the repository
git clone https://github.com/nicholasfocke/clinic-api.git

# Enter the project folder
cd clinic-api

# Run the application
./mvnw spring-boot:run
```

---

## API Documentation

Swagger UI will be available at:
```
http://localhost:8080/swagger-ui.html
```

---

## Author

**Nicholas Focke**
[GitHub](https://github.com/nicholasfocke) · [LinkedIn](https://www.linkedin.com/in/nicholas-focke-833049269)

---

> This project is under active development as part of a backend engineering learning path focused on real-world system design and best practices.
