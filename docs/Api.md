# API Documentation

## Overview

The Container Analyzer API provides endpoints for managing customers, clusters, Docker nodes, and container metrics.

Base URL:

```text
http://localhost:8080
```

All request and response bodies use JSON unless otherwise specified.

---

## HTTP Status Codes

| Status Code | Description                    |
| ----------- | ------------------------------ |
| 200         | Request completed successfully |
| 201         | Resource created successfully  |
| 400         | Invalid request                |
| 404         | Resource not found             |
| 409         | Resource conflict              |
| 500         | Internal server error          |

---

## User Endpoints

### Signup

Creates a new User

```
POST /users/signUp
```

### Request Body

```
{
    "login": "John Smith ",
    "password": "1234"
}
```



## Customer Endpoints

### Create Customer

Creates a new customer.

```http
POST /customer/create
```

#### Request Body

```json
{
  "name": "John Doe",
  "company": "Example Company",
  "email": "john@example.com"
}
```

#### Success Response

```http
200 OK
```

```json
{
  "id": 1,
  "name": "John Doe",
  "company": "Example Company",
  "email": "john@example.com"
}
```

---

### Find Customer by ID

Returns a customer by its identifier.

```http
GET /customer/{id}
```

#### Path Parameters

| Parameter | Type | Description         |
| --------- | ---- | ------------------- |
| id        | Long | Customer identifier |

#### Example Request

```http
GET /customer/1
```

#### Success Response

```http
200 OK
```

```json
{
  "id": 1,
  "name": "John Doe",
  "company": "Example Company",
  "email": "john@example.com"
}
```

#### Error Response

```http
404 Not Found
```

```json
{
  "message": "Customer not found"
}
```

---

### Update Customer

Updates an existing customer.

```http
PUT /customer/{id}
```

#### Path Parameters

| Parameter | Type | Description         |
| --------- | ---- | ------------------- |
| id        | Long | Customer identifier |

#### Request Body

```json
{
  "name": "John Doe",
  "company": "Updated Company",
  "email": "john@example.com"
}
```

#### Success Response

```http
200 OK
```

---

### Delete Customer

Deletes a customer.

```http
DELETE /customer/{id}
```

#### Success Response

```http
200 OK
```

```text
Customer deleted successfully
```

---

## Cluster Endpoints

### List Clusters

Returns all registered clusters.

```http
GET /cluster
```

#### Success Response

```http
200 OK
```

```json
[
  {
    "id": 1,
    "networkName": "container-network"
  }
]
```

---

### Find Cluster by ID

```http
GET /cluster/{id}
```

#### Path Parameters

| Parameter | Type | Description        |
| --------- | ---- | ------------------ |
| id        | Long | Cluster identifier |

---

### Create Cluster

Creates a cluster associated with a customer.

```http
POST /cluster/create/{customerId}
```

#### Path Parameters

| Parameter  | Type | Description                    |
| ---------- | ---- | ------------------------------ |
| customerId | Long | Customer that owns the cluster |

#### Request Body

```json
{
  "networkName": "container-network"
}
```

#### Success Response

```http
200 OK
```

```json
{
  "id": 1,
  "networkName": "container-network"
}
```

---

## Node Endpoints

### List Nodes

Returns all registered Docker nodes.

```http
GET /nodes
```

#### Success Response

```http
200 OK
```

```json
[
  {
    "id": 1,
    "name": "nginx-node",
    "ipAddress": "172.17.0.2",
    "containerId": "a1b2c3d4",
    "image": "nginx",
    "status": "RUNNING",
    "ports": "80/tcp",
    "command": "nginx -g daemon off;",
    "createdAt": "2026-06-18T18:25:18"
  }
]
```

---

### Find Node by ID

Returns a node by its identifier.

```http
GET /nodes/{id}
```

#### Path Parameters

| Parameter | Type | Description     |
| --------- | ---- | --------------- |
| id        | Long | Node identifier |

---

### Create Standalone Node

Creates a Docker container associated directly with a customer.

```http
POST /nodes/{customerId}/create-node
```

#### Path Parameters

| Parameter  | Type | Description         |
| ---------- | ---- | ------------------- |
| customerId | Long | Customer identifier |

#### Request Parameters

| Parameter | Type   | Description           |
| --------- | ------ | --------------------- |
| nodeName  | String | Docker container name |

#### Example Request

```http
POST /nodes/1/create-node?nodeName=nginx-node
```

#### Success Response

```http
200 OK
```

```json
{
  "id": 1,
  "name": "nginx-node",
  "image": "nginx",
  "status": "RUNNING"
}
```

---

### Create Node in Cluster

Creates a Docker node associated with a cluster.

```http
POST /nodes/{clusterId}/nodes
```

#### Path Parameters

| Parameter | Type | Description        |
| --------- | ---- | ------------------ |
| clusterId | Long | Cluster identifier |

#### Request Parameters

| Parameter | Type   | Description           |
| --------- | ------ | --------------------- |
| nodeName  | String | Docker container name |

---

### Delete Node

Deletes a node from the database.

```http
DELETE /nodes/{id}
```

> Depending on the current implementation, deleting a node record may not automatically remove the Docker container.

---

## Metric Endpoints

### List Node Metrics

Returns all metrics associated with a node.

```http
GET /metrics/{nodeId}
```

#### Path Parameters

| Parameter | Type | Description     |
| --------- | ---- | --------------- |
| nodeId    | Long | Node identifier |

#### Success Response

```http
200 OK
```

```json
[
  {
    "id": 1,
    "cpuUsage": 0.0,
    "memoryUsage": 0.05,
    "memoryLimit": 15.54,
    "diskUsage": 0.0,
    "networkUsage": 1.29,
    "timestamp": "2026-06-18T18:25:18"
  }
]
```

---

### Get Latest Node Metric

Returns the most recently collected metric for a node.

```http
GET /metrics/last/{nodeId}
```

#### Success Response

```http
200 OK
```

```json
{
  "id": 10,
  "cpuUsage": 0.0,
  "memoryUsage": 0.05,
  "memoryLimit": 15.54,
  "diskUsage": 0.0,
  "networkUsage": 1.29,
  "timestamp": "2026-06-18T18:25:18"
}
```

---

### Create Metric

Creates a metric record for a node.

```http
POST /metrics/create
```

#### Request Body

```json
{
  "nodeId": 1,
  "cpuUsage": 0.0,
  "memoryUsage": 0.05,
  "memoryLimit": 15.54,
  "diskUsage": 0.0,
  "networkUsage": 1.29
}
```

#### Success Response

```http
200 OK
```

---

## Data Types

### NodeStatus

The `status` field may contain one of the following values:

| Value      | Description                        |
| ---------- | ---------------------------------- |
| RUNNING    | The container is currently running |
| STOPPED    | The container is stopped           |
| PAUSED     | The container is paused            |
| RESTARTING | The container is restarting        |

---

## Notes

* Docker commands are currently executed through WSL.
* Node information is collected from the Docker Engine.
* Metrics are collected using `docker stats --no-stream`.
* Some endpoints may return plain-text messages instead of JSON.
* Authentication is not currently required for the documented endpoints.
