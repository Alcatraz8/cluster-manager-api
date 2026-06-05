# 🧠 Container Manager

Container Manager is a backend system built with Spring Boot for managing Docker containers, nodes, clusters, and customers.

The system scans Docker environments, maps containers into structured nodes, and exposes them through a REST API.

This project was built as a portfolio to demonstrate backend engineering, infrastructure concepts, and container orchestration fundamentals.

---

# Technologies

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Docker
- H2 Database (development)
- PostgreSQL (future)
- Kubernetes (future)

---

# Features

- Customer management (CRUD)
- Node management linked to clusters
- Docker container discovery (docker ps integration)
- Automatic detection of running/stopped containers
- REST API
- JSON View serialization

---

# Environment Requirements

Before running the project, make sure you have:

- JDK 17+
- Maven 3.9+
- Docker installed and running
- Postman (for API testing)

---

# API Testing (Postman)

This project includes a Postman collection for testing all endpoints.

### Collection

You can import the collection:

Postman Collection available in /postman folder
