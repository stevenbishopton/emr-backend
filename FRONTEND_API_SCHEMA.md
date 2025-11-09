# Hospital EMR Frontend API Schema

This document outlines the data structures and their relationships in the Hospital EMR system. Use this as a reference when building the frontend to ensure proper data handling and API integration.

## Table of Contents
1. [Core Models](#core-models)
2. [Enums](#enums)
3. [API Endpoints](#api-endpoints)
4. [Data Relationships](#data-relationships)
5. [Frontend Implementation Notes](#frontend-implementation-notes)
6. [Example API Calls](#example-api-calls)

## Core Models

### Visit & Department Management
```typescript
interface VisitDTO {
  id: number;
  visitDateTime: string;      // ISO datetime string
  status: VisitStatus;        // e.g., IN_QUEUE, COMPLETED
  notes?: string;             // Optional visit notes
  patientId: number;          // Reference to patient
  patientName: string;        // For display purposes
  departments: VisitDepartmentDTO[];  // Departments involved in this visit
  createdAt: string;          // ISO datetime string
  updatedAt: string;          // ISO datetime string
}

interface VisitDepartmentDTO {
  id: number;
  departmentId: number;
  departmentName: string;     // For display purposes
  status: string;             // Status within this department
  notes?: string;             // Department-specific notes
  startTime: string;          // ISO datetime string
  endTime?: string;           // Optional, ISO datetime string
}

interface DepartmentDTO {
  id: number;
  name: string;               // e.g., 'Cardiology', 'Pediatrics'
  description?: string;       // Optional department description
  active: boolean;            // Whether the department is active
}
```

### Ward Management
```typescript
interface WardDTO {
  id: number;
  name: string;               // Ward name (e.g., 'Pediatric Ward A')
  capacity: number;           // Maximum number of patients
  currentOccupancy: number;   // Current number of patients
  departmentId?: number;      // Optional department association
  departmentName?: string;    // For display purposes
  status: WardStatus;         // e.g., AVAILABLE, FULL, MAINTENANCE
  notes?: string;             // Any special notes about the ward
}

interface AdmissionDTO {
  id: number;
  patientId: number;
  patientName: string;        // For display purposes
  wardId: number;
  wardName: string;           // For display purposes
  admissionDate: string;      // ISO datetime string
  dischargeDate?: string;     // Optional, ISO datetime string
  status: AdmissionStatus;    // e.g., ADMITTED, DISCHARGED
  notes?: string;             // Admission notes
  bedNumber: string;          // Specific bed assignment
  primaryDoctorId: number;    // Attending physician
  primaryDoctorName: string;  // For display purposes
}
```

### Patient
```typescript
interface PatientDTO {
  id: number;
  code: string;          // Unique patient identifier
  names: string;         // Full name
  sex: Sex;              // MALE or FEMALE
  dateOfBirth: string;   // ISO date string
  phoneNumber: string;
  email?: string;        // Optional
  address?: string;      // Optional
  occupation?: string;   // Optional
  medicalHistoryId: number;
  nextOfKin?: {
    names: string;
    phoneNumber: string;
    relationship: string;
  };
  bills?: BillDTO[];     // Optional, only included when needed
}
```

### Medical History
```typescript
interface MedicalHistoryDTO {
  id: number;
  patientId: number;
  patientNames: string;
  patientCode: string;
  diagnosisNotes: NoteDTO[];
  clinicalNotes: NoteDTO[];
  prescriptions: PrescriptionDTO[];
  vitalSignsList: VitalSignsDTO[];
  admissions: AdmissionDTO[];
}
```

### Vital Signs
```typescript
interface VitalSignsDTO {
  id: number;
  timeTaken: string;     // ISO datetime string
  temperature?: number;  // °C
  pulseRate?: number;    // bpm
  respiratoryRate?: number; // breaths/min
  systolicBp?: number;   // mmHg
  diastolicBp?: number;  // mmHg
  oxygenSaturation?: number; // %
  bloodGlucose?: number; // mg/dL or mmol/L
  weight?: number;       // kg
  height?: number;       // cm
  bmi?: number;          // kg/m²
  author: string;
  medicalHistoryId: number;
}
```

### Prescription
```typescript
interface PrescriptionDTO {
  id: number;
  prescriptionEntries: PrescriptionEntryDTO[];
  additionalInstructions?: string;
  prescriberId: number;  // Doctor ID
  medicalHistoryId: number;
  createdAt: string;     // ISO datetime string
  updatedAt: string;     // ISO datetime string
}

interface PrescriptionEntryDTO {
  id: number;
  prescriptionId: number;
  itemId: number;
  itemName: string;
  dosage: string;
  route: string;        // e.g., "oral", "IV"
  frequency: string;    // e.g., "QID", "BID"
  durationDays: number;
}
```

### Admission
```typescript
interface AdmissionDTO {
  id: number;
  patientNames: string;
  patientCode: string;
  wardId: number;
  wardName: string;
  medicalHistoryId: number;
  prescriptionChart: PrescriptionChartDTO;
  notes: NoteDTO[];
  admissionRecord: NoteDTO;
  admissionDate: string;  // ISO datetime string
  dischargeDate?: string; // Optional, ISO datetime string
}

interface PrescriptionChartDTO {
  id: number;
  admissionId: number;
  prescriptionEntries: AdmissionPrescriptionEntryDTO[];
}

interface AdmissionPrescriptionEntryDTO {
  id: number;
  item: ItemDTO;
  dosage: string;
  route: string;
  admissionId: number;
  createdAt: string;     // ISO datetime string
  updatedAt: string;     // ISO datetime string
}
```

### Pharmacy
```typescript
interface ItemDTO {
  id: number;
  itemType: ItemType;    // DRUG or UTILITY
  name: string;
  expirationDate?: string; // ISO date string
  quantity: number;
  costPrice?: number;    // Optional
  sellingPrice: number;
  description?: string;  // Optional
  link?: string;         // Optional, e.g., to product information
}
```

### Billing
```typescript
interface BillDTO {
  id: number;
  patientId: number;
  patientNames: string;
  totalAmount: number;
  note?: string;         // Optional
  dateIssued: string;    // ISO date string
  subBills: SubBill[];   // Breakdown of charges
}
```

### Personnel
```typescript
interface PersonnelDTO {
  id: number;
  names: string;
  image?: string;        // URL to profile image
  sex: Sex;              // MALE or FEMALE
  phoneNumber: string;
  email?: string;        // Optional
  address?: string;      // Optional
  username: string;
  personnelType: PersonnelType; // DOCTOR, NURSE, etc.
  departmentId?: number;  // Optional
}
```

### Notes
```typescript
interface NoteDTO {
  id: number;
  noteType: NoteType;    // ADMISSION_NOTE, CLINICAL_NOTE, DIAGNOSIS_NOTE
  content: string;
  author: string;        // Name of the person who wrote the note
  medicalHistoryId: number;
}
```

## Enums

### Sex
```typescript
enum Sex {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}
```

### PersonnelType
```typescript
enum PersonnelType {
  DOCTOR = 'DOCTOR',
  PATIENT = 'PATIENT',
  NURSE = 'NURSE',
  ADMIN = 'ADMIN',
  PHARMACIST = 'PHARMACIST',
  LAB_PERSONNEL = 'LAB_PERSONNEL',
  RECEPTIONIST = 'RECEPTIONIST',
  SECURITY = 'SECURITY'
}
```

### NoteType
```typescript
enum NoteType {
  ADMISSION_NOTE = 'ADMISSION_NOTE',
  CLINICAL_NOTE = 'CLINICAL_NOTE',
  DIAGNOSIS_NOTE = 'DIAGNOSIS_NOTE'
}
```

### ItemType (Pharmacy)
```typescript
enum ItemType {
  DRUG = 'DRUG',
  UTILITY = 'UTILITY'
}
```

### Visit & Department Statuses
```typescript
enum VisitStatus {
  SCHEDULED = 'SCHEDULED',
  IN_QUEUE = 'IN_QUEUE',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  NO_SHOW = 'NO_SHOW'
}

enum DepartmentStatus {
  AVAILABLE = 'AVAILABLE',
  BUSY = 'BUSY',
  CLOSED = 'CLOSED',
  MAINTENANCE = 'MAINTENANCE'
}

enum WardStatus {
  AVAILABLE = 'AVAILABLE',
  OCCUPIED = 'OCCUPIED',
  MAINTENANCE = 'MAINTENANCE',
  RESERVED = 'RESERVED'
}

enum AdmissionStatus {
  PENDING = 'PENDING',
  ADMITTED = 'ADMITTED',
  DISCHARGED = 'DISCHARGED',
  TRANSFERRED = 'TRANSFERRED'
}
```

## Data Relationships

1. **Patient**
   - Has one MedicalHistory (1:1)
   - Has many Bills (1:N)
   - Has many Visits (1:N)
   - Has many Admissions (1:N)

2. **Visit**
   - Belongs to a Patient (N:1)
   - Has many VisitDepartments (1:N)
   - References multiple Departments through VisitDepartment (N:M)

3. **VisitDepartment**
   - Belongs to a Visit (N:1)
   - Belongs to a Department (N:1)
   - Tracks department-specific visit status and notes

4. **Department**
   - Has many VisitDepartments (1:N)
   - Can be associated with multiple Wards (1:N)
   - Manages specific medical specialties

5. **Ward**
   - Belongs to a Department (N:1, optional)
   - Has many Admissions (1:N)
   - Tracks bed capacity and occupancy

6. **Admission**
   - Belongs to a Patient (N:1)
   - Belongs to a Ward (N:1)
   - Has one PrescriptionChart (1:1)
   - Has one Admission Note (1:1, through Notes with type ADMISSION_NOTE)

7. **MedicalHistory**
   - Has many Notes (1:N, with type)
   - Has many Prescriptions (1:N)
   - Has many VitalSigns (1:N)
   - Has many Admissions (1:N)
   - Has many LabDiagnosticReports (1:N)

8. **Prescription**
   - Has many PrescriptionEntries (1:N)
   - Belongs to a MedicalHistory (N:1)
   - References a Doctor (N:1, through prescriberId)

## Frontend Implementation Notes

1. **Date/Time Handling**
   - All dates are in ISO 8601 format (e.g., "2023-01-15T14:30:00Z")
   - Use a robust date library like `date-fns` or `moment.js`

2. **Form Validation**
   - Required fields are marked with `@NotNull` in the backend
   - Implement client-side validation for better UX
   - Validate email formats, phone numbers, etc.

3. **Error Handling**
   - Handle 401/403 for authentication/authorization
   - Show user-friendly error messages
   - Implement retry logic for failed requests

4. **Pagination**
   - Use pagination for large datasets (e.g., patient lists, visit history)
   - Implement infinite scroll or traditional pagination controls

5. **Real-time Updates**
   - Consider using WebSockets for real-time updates (e.g., new lab results, vital signs)
   - Implement optimistic UI updates where appropriate

6. **Security**
   - Store JWT tokens securely (httpOnly cookies recommended)
   - Implement proper session timeout
   - Follow least privilege principle for user roles

## Example API Calls

### Visit Management
```http
# Create a new visit
POST /api/visits
Body: {
  "patientId": 123,
  "visitDateTime": "2023-01-15T09:00:00Z",
  "notes": "Routine checkup",
  "departmentIds": [1, 2]  // Departments to visit
}
Response: VisitDTO

# Update visit status
PATCH /api/visits/{visitId}/status
Body: {
  "status": "IN_PROGRESS",
  "departmentId": 1  // Optional, if updating specific department status
}

# Get patient's visit history
GET /api/patients/{patientId}/visits
Query Params: {
  status: 'COMPLETED',  // Optional filter
  from: '2023-01-01',   // Optional date range
  to: '2023-01-31'
}
Response: VisitDTO[]
```

### Ward & Admission Management
```http
# Get all wards with availability
GET /api/wards
Query Params: {
  status: 'AVAILABLE',  // Optional filter
  departmentId: 1       // Optional filter by department
}
Response: WardDTO[]

# Admit a patient to a ward
POST /api/admissions
Body: {
  "patientId": 123,
  "wardId": 5,
  "bedNumber": "A12",
  "primaryDoctorId": 45,
  "admissionNotes": "Patient admitted for observation"
}
Response: AdmissionDTO

# Update admission status
PATCH /api/admissions/{admissionId}
Body: {
  "status": "DISCHARGED",
  "dischargeDate": "2023-01-20T14:00:00Z",
  "dischargeNotes": "Patient recovered well"
}
```

### Department Management
```http
# Get all departments
GET /api/departments
Response: DepartmentDTO[]

# Get department details with current visit stats
GET /api/departments/{id}/stats
Response: {
  "department": DepartmentDTO,
  "currentVisits": number,
  "inQueue": number,
  "averageWaitTime": number  // in minutes
}
```

### Get Patient by ID
```http
GET /api/patients/{id}
Response: PatientDTO
```

### Get Patient's Medical History
```http
GET /api/medical-history?patientId={id}
Response: MedicalHistoryDTO
```

### Create New Prescription
```http
POST /api/prescriptions
Body: {
  "prescriptionEntries": [{
    "itemId": 1,
    "dosage": "500mg",
    "route": "oral",
    "frequency": "QID",
    "durationDays": 7
  }],
  "medicalHistoryId": 123,
  "prescriberId": 456
}
Response: PrescriptionDTO
```

### Record Vital Signs
```http
POST /api/vital-signs
Body: {
  "temperature": 36.8,
  "pulseRate": 72,
  "respiratoryRate": 16,
  "systolicBp": 120,
  "diastolicBp": 80,
  "oxygenSaturation": 98,
  "medicalHistoryId": 123,
  "author": "Dr. Smith"
}
Response: VitalSignsDTO
```

This documentation should provide a solid foundation for building the frontend. The AI agent can use this to generate forms, tables, and other UI components that match the backend data structure.
