# Banking Async Processing Framework

## Overview

The Banking Async Processing Framework is an enterprise-grade Spring Boot application designed to handle long-running banking operations asynchronously without blocking user requests. The system leverages Spring Async, CompletableFuture, and ThreadPoolTaskExecutor to execute background tasks efficiently while providing job tracking, audit logging, retry handling, and monitoring capabilities.

## Business Scenario

In banking systems, operations such as statement generation, report exports, email notifications, and bulk customer imports can take significant time to complete. Executing these operations synchronously can impact user experience and application performance.

This framework allows such operations to run in the background while immediately returning a Job ID to the user for tracking execution status.

---

## Features

### Asynchronous Processing
- Spring Async (`@Async`)
- CompletableFuture
- ThreadPoolTaskExecutor

### Banking Operations
- Customer Statement Generation (PDF)
- Report Export Processing
- Email Notification Processing
- Bulk Customer Import via CSV

### Job Management
- Start Async Jobs
- Track Job Status
- View Job History
- List Running Jobs
- Cancel Running Jobs

### Reliability
- Retry Mechanism for Failed Operations
- Exception Handling
- Failure Recovery

### Auditing
- Audit Log Creation
- Execution Tracking
- Success/Failure Logging

### Monitoring
- Spring Boot Actuator
- Thread Pool Monitoring
- Job Execution Monitoring

---

## Technology Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Async
- CompletableFuture
- MySQL
- OpenPDF
- Spring Mail
- Spring Boot Actuator
- Lombok
- Swagger/OpenAPI

---

## Architecture

```text
Controller
    |
    V
Service Layer
    |
    V
Async Processor Layer (@Async)
    |
    +---- Statement Generation
    +---- Report Export
    +---- Email Notification
    +---- Customer Import
    |
    V
Database
    |
    +---- async_jobs
    +---- audit_logs
    +---- customers
    +---- accounts
    +---- transactions
```

---

## Database Tables

### customers

Stores customer information.

| Column | Type |
|----------|----------|
| customer_id | BIGINT |
| customer_name | VARCHAR |
| email | VARCHAR |
| mobile | VARCHAR |

---

### accounts

Stores account information.

| Column | Type |
|----------|----------|
| account_id | BIGINT |
| account_number | VARCHAR |
| account_type | VARCHAR |
| balance | DOUBLE |
| customer_id | BIGINT |

---

### transactions

Stores customer transactions.

| Column | Type |
|----------|----------|
| transaction_id | BIGINT |
| transaction_type | VARCHAR |
| amount | DOUBLE |
| transaction_date | DATETIME |
| account_id | BIGINT |

---

### async_jobs

Stores background job information.

| Column | Type |
|----------|----------|
| job_id | BIGINT |
| job_type | VARCHAR |
| status | VARCHAR |
| start_time | DATETIME |
| end_time | DATETIME |
| result_message | VARCHAR |
| error_message | VARCHAR |

---

### audit_logs

Stores execution audit logs.

| Column | Type |
|----------|----------|
| id | BIGINT |
| job_id | BIGINT |
| action | VARCHAR |
| status | VARCHAR |
| message | VARCHAR |
| timestamp | DATETIME |

---

## Job Lifecycle

```text
PENDING
   |
   V
RUNNING
   |
   +---- COMPLETED
   |
   +---- FAILED
   |
   +---- CANCELLED
```

---

## APIs

### Statement Generation

```http
POST /api/statements/generate/{customerId}
```

Response

```json
{
  "jobId": 1
}
```

---

### Export Report

```http
POST /api/reports/export
```

Response

```json
{
  "jobId": 2
}
```

---

### Send Email Notification

```http
POST /api/emails/send
```

Request

```json
{
  "to": "customer@gmail.com",
  "subject": "Welcome",
  "body": "Welcome to our bank"
}
```

---

### Import Customers

```http
POST /api/customers/import
```

Form Data

```text
file : customers.csv
```

---

### Get Job Status

```http
GET /api/jobs/{jobId}
```

---

### List Running Jobs

```http
GET /api/jobs/running
```

---

### View Job History

```http
GET /api/jobs/history
```

---

### Cancel Job

```http
POST /api/jobs/cancel/{jobId}
```

---

## Thread Pool Configuration

```java
Core Pool Size : 5
Maximum Pool Size : 10
Queue Capacity : 50
Thread Prefix : bank-executor-
```

This configuration allows multiple banking operations to execute concurrently while maintaining resource efficiency.

---

## Audit Logging

Every async operation creates audit records.

Examples:

- Statement Generation Started
- Statement Generation Completed
- Email Sent Successfully
- Customer Import Failed
- Job Cancelled

---

## Retry Mechanism

Failed operations can be retried automatically.

Examples:

- Email Server Temporary Failure
- Report Export Failure
- External Service Timeout

---

## Statement Generation

The system generates PDF bank statements containing:

- Customer Information
- Account Information
- Transaction History
- Account Balance

Generated files are stored in the configured statement directory.

---

## Monitoring

Spring Boot Actuator provides:

```http
/actuator/health
/actuator/metrics
/actuator/info
```

---

## Performance Benefits

### Without Async Processing

```text
User Request
    |
Wait 15-30 seconds
    |
Response
```

### With Async Processing

```text
User Request
    |
Immediate Job ID Response
    |
Background Processing
    |
Status Tracking
```

Benefits:

- Faster API Response Time
- Better User Experience
- Improved Scalability
- Efficient Resource Utilization

---

## Future Enhancements

- Scheduled Job Execution
- Dashboard UI
- Role-Based Security
- Real-Time Notifications
- Cloud Storage Integration
- Distributed Processing
- Advanced Metrics Dashboard

---

## Author

Mahesh Kapilavai

Banking Async Processing Framework

Spring Boot | Async Processing | Job Tracking | Audit Logging | Monitoring