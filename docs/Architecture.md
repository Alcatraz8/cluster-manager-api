# Application Architecture

## Overview

Container Analyzer follows a layered architecture to separate responsibilities and keep the code organized, maintainable, and easier to test.

The application is divided into the following main layers:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
```

The application also communicates with the Docker Engine to create containers, inspect their status, and collect runtime metrics.

```text
Client
  ↓
REST API
  ↓
Service Layer
  ├── Repository Layer → PostgreSQL
  └── Docker Service → Docker Engine
```

---

## Architecture Layers

### Controller Layer

The controller layer exposes the REST API endpoints and receives HTTP requests from clients.

Its main responsibilities are:

* Receive request parameters and request bodies.
* Call the appropriate service methods.
* Return HTTP responses.
* Define the available API routes.

Main controllers:

* `CustomerController`
* `ClusterController`
* `NodeController`
* `MetricController`

Controllers should not contain business logic.

---

### Service Layer

The service layer contains the main business logic of the application.

Its responsibilities include:

* Validating application rules.
* Creating and managing customers, clusters, and nodes.
* Communicating with repositories.
* Executing Docker commands.
* Collecting and processing container metrics.

Main services:

* `CustomerService`
* `ClusterService`
* `NodeService`
* `MetricService`
* `DockerService`

---

### Repository Layer

The repository layer is responsible for database communication.

The project uses Spring Data JPA repositories to perform CRUD operations and custom queries.

Main repositories:

* `CustomerRepository`
* `ClusterRepository`
* `NodeRepository`
* `MetricRepository`
* `UserRepository`

---

### Entity Layer

The entity layer represents the application data model.

The main entities are:

* `User`
* `Customer`
* `Cluster`
* `Node`
* `Metric`

Entity relationships and field descriptions are documented in [Entities](entities.md).

---

## Docker Integration

The application communicates with Docker through operating system commands executed using Java `ProcessBuilder`.

The Docker integration is responsible for:

* Creating containers.
* Listing active and inactive containers.
* Inspecting container information.
* Reading container status.
* Collecting CPU, memory, network, and disk metrics.

The commands are executed through WSL in the current development environment.

Example flow:

```text
NodeService
  ↓
DockerService
  ↓
ProcessBuilder
  ↓
WSL
  ↓
Docker Engine
```

---

## Metrics Collection

The metric collection process retrieves runtime information from Docker containers.

The application uses the `docker stats --no-stream` command to collect:

* CPU usage.
* Memory usage.
* Memory limit.
* Network usage.
* Disk usage.

The collected data is processed by `MetricService` and stored in PostgreSQL as `Metric` records associated with a `Node`.

```text
Docker Engine
  ↓
DockerService
  ↓
MetricService
  ↓
MetricRepository
  ↓
PostgreSQL
```

---

## Container Discovery

When the application starts, it can inspect existing Docker containers and register containers that are not yet stored in the database.

This process helps synchronize the application state with the Docker environment.

```text
Application Startup
  ↓
Docker container discovery
  ↓
Check existing Node records
  ↓
Register missing containers
```

---

## Database

PostgreSQL is used as the main persistent database.

The database stores:

* Users.
* Customers.
* Clusters.
* Nodes.
* Container metrics.

Spring Data JPA and Hibernate are responsible for mapping Java entities to database tables.

---

## Request Flow Example

The following example shows the flow used to create a node:

```text
POST request
  ↓
NodeController
  ↓
NodeService
  ↓
DockerService
  ↓
Docker Engine
  ↓
NodeRepository
  ↓
PostgreSQL
  ↓
HTTP response
```

The service first creates the Docker container, collects its information, creates the corresponding `Node` entity, and saves it in the database.

---

## Design Decisions

### Layered Architecture

A layered architecture was chosen to separate API handling, business logic, and database access.

### DockerService as an Auxiliary Service

`DockerService` centralizes Docker command execution and parsing. It is used by other services such as `NodeService` and `MetricService`.

### PostgreSQL Persistence

PostgreSQL is used instead of an in-memory database so application data remains available after restarts.

### Entity Relationships

JPA relationships are used to represent the connections between customers, clusters, nodes, and metrics.

---

## Current Limitations

* Docker commands currently depend on WSL.
* Docker integration is based on command execution using `ProcessBuilder`.
* Authentication and authorization are still under development.
* Kubernetes integration is planned for a future version.

---

## Future Improvements

* Replace direct Docker commands with a Docker API client.
* Add Kubernetes support.
* Add scheduled metric collection.
* Add centralized exception handling.
* Add DTOs for request and response objects.
* Add unit and integration tests.
* Add a frontend monitoring dashboard.
