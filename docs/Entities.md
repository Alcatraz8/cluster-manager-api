# Entities

## User

Represents an authenticated user of the system.

### Fields

| Field | Type |
|--------|------|
| id | Long |
| login | String |
| password | String |

### Relationships

- One-to-One → Customer

---

## Customer

Represents a client who owns one or more clusters.

### Fields

| Field | Type |
|--------|------|
| id | Long |
| name | String |
| company | String |
| email | String |

### Relationships

- One-to-One → User
- One-to-Many → Cluster

---

## Cluster

Represents a group of managed Docker nodes.

### Fields

| Field | Type |
|--------|------|
| id | Long |
| networkName | String |

### Relationships

- Many-to-One → Customer
- One-to-Many → Node

## Node

Represents a Docker container managed by the system. 
A node may belong to a cluster or exist as a standalone container.

### Fields

| Field       | Type          |
|-------------|---------------|
| id          | Long          |
| ipAddress   | String        |
| containerId | String        |
| image       | String        |
| ports       | String        |
| command     | String        |
| createdAt   | LocalDateTime |
| status      | NodeStatus    |

### Relationships

- Many-to-One → Customer
- Many-to-One → Cluster
- One-to-Many → Metric

## NodeStatus

Represents the possible states of a node.

### Fields

| Field | Type |
|-------|------|
| RUNNING | enum |
| STOPPED | enum |
| PAUSED  | enum |
| RESTARTING | enum |

## Metric

Represents resource usage metrics collected from a Docker node.

### Fields
| Field        | Type          |
|--------------|---------------|
| id           | Long          |
| cpuUsage     | Double        |
| memoryUsage  | Double        |
| diskUsage    | Double        |
| networkUsage | Double        |
| memoryLimit  | Double        |
| timestamp    | LocalDateTime |

### Relationships

- Many-to-One → Node