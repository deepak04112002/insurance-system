# Insurance Claim Management System

## Overview
REST API for managing insurance policies, policyholders, and claims with JWT authentication and role-based access control.

## Tech Stack
- Spring Boot 3.x
- Spring Security with JWT
- MySQL Database
- Swagger/OpenAPI Documentation
- File Upload Support
- JUnit 5 Testing

## Build & Run Instructions

### Prerequisites
- Java 21
- Maven 3.6+
- MySQL 8.0+

### Setup Steps

1. **Clone Repository**
   ```bash
   git clone <repository-url>
   cd insurance-system
   ```
   
2. **Database Setup**
    ```bash
    # Start MySQL server
    mysql -u root -p
    # Run schema.sql file
    source schema.sql
    ```
   
3. **Configure Database**
   - Update src/main/resources/application.properties
   - Set your MySQL username/password

4. **Build & Run**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
   
5. **Access Application**
   - Application: http://localhost:8081
   - Swagger UI: http://localhost:8081/swagger-ui/index.html

### API Testing with Postman/cURL

#### Authentication APIs

1. **Register Admin Use**
   ```bash
   curl -X POST http://localhost:8081/api/auth/register \
   -H "Content-Type: application/json" \
   -d '{
   "name": "Admin User",
   "email": "admin@example.com",
   "password": "admin123",
   "role": "ADMIN"
   }'
   ```
   
2. **Register Agent User**
   ```bash
   curl -X POST http://localhost:8081/api/auth/register \
   -H "Content-Type: application/json" \
   -d '{
   "name": "Alice Agent",
   "email": "alice@example.com",
   "password": "pass123",
   "role": "AGENT"
   }'
   ```
   
3. **Login (Admin)**
   ```bash
   curl -X POST http://localhost:8081/api/auth/login \
   -H "Content-Type: application/json" \
   -d '{
   "email": "admin@example.com",
   "password": "admin123"
   }'
   ```
   
4. **Login (Agent)**
   ```bash
   curl -X POST http://localhost:8081/api/auth/login \
   -H "Content-Type: application/json" \
   -d '{
   "email": "alice@example.com",
   "password": "pass123"
   }'
   ```
   
#### PolicyHolder APIs (ADMIN Only)

5. **Create PolicyHolder**
   ```bash
   curl -X POST http://localhost:8081/api/policyholders \
   -H "Authorization: Bearer <ADMIN_JWT_TOKEN>" \
   -H "Content-Type: application/json" \
   -d '{
   "name": "John Smith",
   "email": "john@example.com",
   "dob": "1985-07-21",
   "phone": "9876543210"
   }'
   ```
   
6. **Get PolicyHolder by ID**
   ```bash
   curl -X GET http://localhost:8081/api/policyholders/1 \
   -H "Authorization: Bearer <ADMIN_JWT_TOKEN>"
   ```
   
#### Policy APIs

7. **Create Policy (ADMIN)**
   ```bash
   curl -X POST http://localhost:8081/api/policies \
   -H "Authorization: Bearer <ADMIN_JWT_TOKEN>" \
   -H "Content-Type: application/json" \
   -d '{
   "policyNumber": "POL123456",
   "type": "Health",
   "coverageAmount": 500000,
   "startDate": "2024-01-01",
   "endDate": "2029-01-01",
   "policyHolderId": 1
   }'
    ```
   
8. **Get Policy by ID (ADMIN/AGENT)**
   ```bash
   curl -X GET http://localhost:8081/api/policies/1 \
   -H "Authorization: Bearer <ADMIN_OR_AGENT_JWT_TOKEN>"
    ```
   
#### Claim APIs

9. **Create Claim (AGENT)**
   ```bash
   curl -X POST http://localhost:8081/api/claims \
   -H "Authorization: Bearer <AGENT_JWT_TOKEN>" \
   -H "Content-Type: application/json" \
   -d '{
   "claimNumber": "CLM001",
   "claimDate": "2024-12-23",
   "amountClaimed": 50000,
   "status": "PENDING",
   "policyId": 1
   }'
   ```
   
10. **Upload Claim Document (AGENT)**
    ```bash
    curl -X POST http://localhost:8081/api/claims/upload \
      -H "Authorization: Bearer <AGENT_JWT_TOKEN>" \
      -F "claimId=1" \
      -F "file=@document.pdf"
    ```
    
11. **Get Claim by ID (ADMIN/AGENT)**
    ```bash
    curl -X GET http://localhost:8081/api/claims/1 \
    -H "Authorization: Bearer <ADMIN_OR_AGENT_JWT_TOKEN>"
    ```
    
12. **Get All Claims (ADMIN Only)**
    ```bash
    curl -X GET http://localhost:8081/api/claims/all \
    -H "Authorization: Bearer <ADMIN_JWT_TOKEN>"
    ```
    
### Sample Payloads

#### Register Agent
   ```json
   {
      "name": "Alice Agent",
      "email": "alice@example.com",
      "password": "pass123",
      "role": "AGENT"
   }
   ```
#### Create PolicyHolder
   ```json
   {
   "name": "John Smith",
   "email": "john@example.com",
   "dob": "1985-07-21",
   "phone": "9876543210"
   }
   ```
#### Create Claim
   ```json
   {
   "claimNumber": "CLM001",
   "claimDate": "2024-12-23",
   "amountClaimed": 50000,
   "status": "PENDING",
   "policyId": 1
   }
   ```

### API Testing Workflow
   1. Register ADMIN user via /api/auth/register
   2. Register AGENT user via /api/auth/register
   3. Login to get JWT tokens
   4. Use tokens in Swagger "Authorize" button
   5. Test endpoints based on role permissions

### Role-Based Access
  - **ADMIN**: All PolicyHolder and Policy operations
  - **AGENT**: Policy read, Claim operations, File upload
  - **Open**: Registration and Login
  
### File Upload
- Endpoint: ```/api/claims/upload```
- Format: multipart/form-data
- Parameters: claimId, file (PDF/Image)
- Max Size: 10MB

### Sample JWT Token
After login, you'll receive a token like:
```aiignore
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTczNDk2NzIwMCwiZXhwIjoxNzM1MDUzNjAwfQ.sample_signature
```
Use this token in the Authorization header: ```Bearer <your-jwt-token>```





