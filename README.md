# Hospital EMR Backend System

A comprehensive **Electronic Medical Records (EMR)** backend API built with **Spring Boot** for managing hospital operations including patient records, visits, prescriptions, lab tests, billing, and more.

---

## Table of Contents

1. [Tech Stack](#tech-stack)
2. [Prerequisites](#prerequisites)
3. [Project Structure](#project-structure)
4. [Setup Instructions](#setup-instructions)
5. [Running the Application](#running-the-application)
6. [API Documentation](#api-documentation)
7. [Core Modules & Functionality](#core-modules--functionality)
8. [Database Configuration](#database-configuration)
9. [Security & Authentication](#security--authentication)
10. [Environment Variables](#environment-variables)

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| **Spring Boot 3.5.3** | Main framework |
| **Java 17** | Programming language |
| **PostgreSQL** | Primary database (configurable to H2 for dev) |
| **Spring Data JPA** | ORM and database access |
| **Spring Security + JWT** | Authentication & authorization |
| **MapStruct** | Object mapping (DTOs to Entities) |
| **Lombok** | Boilerplate code reduction |
| **Spring WebSocket** | Real-time notifications |
| **RabbitMQ** | Message queuing |
| **Spring Batch** | Batch processing |
| **OpenAPI (Swagger)** | API documentation |

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven** (or use the included `./mvnw` wrapper)
- **PostgreSQL** database (optional - can use H2 for development)
- **Git** for cloning the repository

---

## Project Structure

```
emr/
├── src/main/java/hospital/emr/
│   ├── EmrApplication.java          # Application entry point
│   ├── admin/                        # Administration (personnel, departments, purchases)
│   ├── bill/                         # Billing & invoicing
│   ├── common/                       # Shared utilities, config, base entities
│   ├── doctor/                       # Doctor-specific features
│   ├── lab/                          # Laboratory tests & results
│   ├── notification/                 # Notification system
│   ├── nurse/                        # Nursing reports & tasks
│   ├── patient/                      # Patient management (admissions, prescriptions, vitals)
│   ├── pharmacy/                     # Inventory, prescriptions, billing
│   ├── radiograph/                   # X-ray/imaging management
│   ├── reception/                    # Visit management & registration
│   ├── security/                     # Authentication & JWT
│   └── ward/                         # Ward/bed management
├── src/main/resources/
│   ├── application.properties        # Main configuration
│   └── db/migration/                 # SQL migration files
├── pom.xml                           # Maven dependencies
└── README.md                         # This file
```

---

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd emr
```

### Step 2: Configure the Database

**Option A: PostgreSQL (Recommended for Production)**

1. Create a PostgreSQL database:
```sql
CREATE DATABASE emr_db;
CREATE USER godreigns WITH PASSWORD 'godreigns';
GRANT ALL PRIVILEGES ON DATABASE emr_db TO godreigns;
```

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/emr_db
spring.datasource.username=godreigns
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

**Option B: H2 Database (Quick Development)**

For quick testing, you can switch to H2 in-memory database by changing the properties:
```properties
spring.datasource.url=jdbc:h2:file:~/emr_database
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### Step 3: Install Dependencies

```bash
./mvnw clean install
```

Or on Windows:
```cmd
mvnw.cmd clean install
```

---

## Running the Application

### Development Mode

```bash
./mvnw spring-boot:run
```

The application will start on **http://localhost:8080**

### Build & Run JAR

```bash
./mvnw clean package
java -jar target/emr-0.0.1-SNAPSHOT.jar
```

### Access Points

| URL | Purpose |
|-----|---------|
| http://localhost:8080 | Application base |
| http://localhost:8080/swagger-ui.html | API documentation (Swagger UI) |
| http://localhost:8080/actuator/health | Health check endpoint |

---

## API Documentation

The API is documented using **OpenAPI (Swagger)**. Once running, access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## Core Modules & Functionality

### 1. **Patient Management** (`/patient/`)
- Patient registration & profiles
- Medical history tracking
- Vital signs recording (temperature, BP, pulse, etc.)
- Admissions & ward assignments
- Prescription management

### 2. **Visit & Reception** (`/reception/`)
- Outpatient visit registration
- Department queue management
- Visit status tracking

### 3. **Pharmacy** (`/pharmacy/`)
- Inventory management
- Prescription dispensing
- Nurse item requests
- Patient billing for medications

### 4. **Laboratory** (`/lab/`)
- Lab test catalog management
- Test request processing
- Result entry & reporting
- Integration with patient records

### 5. **Radiology** (`/radiograph/`)
- X-ray/imaging catalog
- Request management
- Visit history tracking
- Result storage

### 6. **Billing** (`/bill/`)
- Patient billing
- Department billing
- Invoice generation
- Payment tracking

### 7. **Ward Management** (`/ward/`)
- Ward/bed inventory
- Patient admissions
- Bed assignment
- Discharge processing

### 8. **Administration** (`/admin/`)
- Personnel management
- Department configuration
- Purchase receipts
- System settings

### 9. **Notifications** (`/notification/`)
- Real-time notifications via WebSocket
- Notification history
- Push notification support

### 10. **Security** (`/auth/`)
- JWT-based authentication
- User login/registration
- Role-based access control

---

## Database Configuration

### Entity Relationship Overview

```
Patient (1) ────> (*) MedicalHistory
MedicalHistory (1) ────> (*) Prescription, VitalSigns, Admissions, Notes
Visit (1) ────> (*) VisitDepartment
Ward (1) ────> (*) Admission
Item/Drug (1) ────> (*) PrescriptionEntry, BillItem
```

### Key Entities

| Entity | Description |
|--------|-------------|
| `Patient` | Core patient demographic data |
| `MedicalHistory` | Aggregates all patient medical data |
| `Visit` | Outpatient visit records |
| `Admission` | Inpatient stay records |
| `Prescription` | Doctor prescriptions |
| `LabTestRequest` | Laboratory test orders |
| `Radiograph` | Imaging records |
| `Bill` | Patient billing information |

---

## Security & Authentication

The system uses **JWT (JSON Web Tokens)** for stateless authentication.

### Authentication Flow

1. **Login**: `POST /api/auth/login`
   ```json
   {
     "username": "user@example.com",
     "password": "password"
   }
   ```

2. **Token Response**:
   ```json
   {
     "accessToken": "eyJhbGciOiJIUzI1NiIs...",
     "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
     "tokenType": "Bearer",
     "expiresIn": 86400000
   }
   ```

3. **Authenticated Requests**: Include the token in the header:
   ```
   Authorization: Bearer <accessToken>
   ```

### JWT Configuration (application.properties)

```properties
security.jwt.secret-key=ZmFrZV9yYW5kb21fbHlyaWNfYXV0aF9rZXlfZm9yX2Vtcl9wcm9qZWN0
security.jwt.expiration-ms=86400000        # 24 hours
security.jwt.refresh-expiration-ms=604800000  # 7 days
```

---

## Environment Variables

For production deployment, override these in your environment:

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/emr_db` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `godreigns` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `godreigns` |
| `SECURITY_JWT_SECRET_KEY` | JWT signing key | (in properties) |
| `SECURITY_JWT_EXPIRATION_MS` | Token expiration | `86400000` |
| `SERVER_PORT` | Application port | `8080` |

---

## Testing the API

### Quick Test with cURL

```bash
# Health check
curl http://localhost:8080/actuator/health

# Get all patients (requires authentication)
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/patients

# Create a patient
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "code": "P001",
    "names": "John Doe",
    "sex": "MALE",
    "dateOfBirth": "1990-01-01",
    "phoneNumber": "+1234567890"
  }'
```

---

## Additional Resources

- **Frontend API Schema**: See `FRONTEND_API_SCHEMA.md` for detailed TypeScript interfaces
- **Lab & Medical History API**: See `LAB_AND_MEDICAL_HISTORY_API_DOCUMENTATION.md`
- **SQL Fixes**: `EXACT_DATABASE_FIX.sql` and `QUICK_FIX.sql` for database troubleshooting

---

## Troubleshooting

### Common Issues

**1. Database Connection Error**
```
Failed to configure a DataSource: 'url' attribute is not specified
```
- Solution: Check `application.properties` database configuration

**2. JWT Token Expired**
```
JWT token is expired or invalid
```
- Solution: Re-authenticate to get a new token

**3. CORS Errors**
- The backend allows all origins (`*`) for development. For production, configure specific origins in `application.properties`.

**4. Port Already in Use**
```
Port 8080 was already in use
```
- Solution: Change `server.port` in `application.properties` or stop the existing process

---

## License

[Add your license information here]

## Support

For issues or questions, please contact [your contact information].
