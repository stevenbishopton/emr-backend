# Lab and Medical History API Documentation

This document contains all the information needed for frontend integration of the Lab Test Results and Medical History modules.

## Table of Contents
1. [Lab Test Results](#lab-test-results)
2. [Medical History](#medical-history)
3. [API Endpoints](#api-endpoints)
4. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
5. [Enums](#enums)
6. [Relationships](#relationships)

---

## Lab Test Results

### Entity: LabTestResult
**Table:** `lab_test_results`

#### Fields
| Field | Type | Description | Required |
|-------|------|-------------|----------|
| `id` | Long | Primary key | Auto-generated |
| `patientId` | Long | Patient ID | Yes |
| `visitId` | Long | Visit ID | No |
| `carriedOutBy` | String | Lab technician who performed the test | No |
| `testIds` | List<Long> | List of test IDs that were performed | Yes |
| `results` | String (TEXT) | Manually typed results by lab technician | No |
| `interpretation` | String (TEXT) | Interpretation of the results | No |
| `comments` | String (TEXT) | Additional comments | No |
| `status` | TestStatus | Status of the test result | Yes (default: PENDING) |
| `requestedBy` | String | Lab technician who requested the test | No |
| `createdAt` | LocalDateTime | Creation timestamp | Auto-generated |
| `updatedAt` | LocalDateTime | Last update timestamp | Auto-generated |
| `completedAt` | LocalDateTime | Completion timestamp | No |
| `medicalHistory` | MedicalHistory | Reference to medical history | Linked automatically |

#### Indexes
- `idx_lab_result_patient` on `patient_id`
- `idx_lab_result_visit` on `visit_id`
- `idx_lab_result_status` on `status`
- `idx_lab_result_created` on `created_at`

---

## Medical History

### Entity: MedicalHistory
**Table:** `medical_history`

#### Fields
| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Primary key |
| `patient` | Patient | One-to-one relationship with Patient |
| `clinicalNotes` | List<Note> | Clinical notes (ordered by createdAt DESC) |
| `diagnosisNotes` | List<Note> | Diagnosis notes (ordered by createdAt DESC) |
| `prescriptions` | List<Prescription> | Prescriptions (ordered by createdAt DESC) |
| `vitalSignsList` | List<VitalSigns> | Vital signs (ordered by timeTaken DESC) |
| `labTestResults` | List<LabTestResult> | Lab test results (ordered by createdAt DESC) |
| `admissions` | List<Admission> | Admissions |

#### Relationships
- **One-to-One** with `Patient`
- **One-to-Many** with `LabTestResult` (bidirectional)
- **One-to-Many** with `Note` (clinical and diagnosis)
- **One-to-Many** with `Prescription`
- **One-to-Many** with `VitalSigns`
- **One-to-Many** with `Admission`

---

## API Endpoints

### Lab Test Results Endpoints
**Base URL:** `/emr/lab/results`

#### 1. Create Lab Test Result
```http
POST /emr/lab/results
Content-Type: application/json

Request Body:
{
  "patientId": 1,
  "visitId": 123,
  "requestedBy": "Dr. Smith",
  "testIds": [1, 2, 3],
  "results": "Test results text...",
  "interpretation": "Normal range",
  "comments": "Additional notes",
  "carriedOutBy": "Lab Tech Name"
}

Response: 201 Created
{
  "id": 1,
  "patientId": 1,
  "visitId": 123,
  "requestedBy": "Dr. Smith",
  "testIds": [1, 2, 3],
  "tests": [...], // Populated with test details
  "results": "Test results text...",
  "interpretation": "Normal range",
  "comments": "Additional notes",
  "status": "PENDING",
  "carriedOutBy": "Lab Tech Name",
  "createdAt": "2026-01-24T10:00:00",
  "updatedAt": "2026-01-24T10:00:00",
  "completedAt": null
}
```

#### 2. Get Lab Test Result by ID
```http
GET /emr/lab/results/{id}

Response: 200 OK
LabTestResultDTO
```

#### 3. Update Lab Test Result
```http
PUT /emr/lab/results/{id}
Content-Type: application/json

Request Body:
{
  "results": "Updated results...",
  "interpretation": "Updated interpretation",
  "comments": "Updated comments",
  "status": "COMPLETED"
}

Response: 200 OK
LabTestResultDTO
```

#### 4. Get All Results for a Patient
```http
GET /emr/lab/results/patient/{patientId}

Response: 200 OK
List<LabTestResultDTO>
```

#### 5. Get All Results for a Visit
```http
GET /emr/lab/results/visit/{visitId}

Response: 200 OK
List<LabTestResultDTO>
```

#### 6. Get Results by Status
```http
GET /emr/lab/results/status/{status}

Status values: PENDING, IN_PROGRESS, COMPLETED, CANCELLED

Response: 200 OK
List<LabTestResultDTO>
```

#### 7. Get Pending Results
```http
GET /emr/lab/results/pending

Response: 200 OK
List<LabTestResultDTO>
```

---

### Medical History Endpoints
**Base URL:** `/emr/medical-history`

#### 1. Create Medical History
```http
POST /emr/medical-history
Content-Type: application/json

Request Body: MedicalHistoryDTO

Response: 200 OK
MedicalHistoryDTO
```

#### 2. Get Medical History for Patient (Ungrouped)
```http
GET /emr/medical-history/patient/{patientId}

Response: 200 OK
{
  "id": 1,
  "patientId": 1,
  "patientNames": "John Doe",
  "patientCode": "P001",
  "diagnosisNotes": [...],
  "clinicalNotes": [...],
  "prescriptions": [...],
  "vitalSignsList": [...],
  "admissions": [...],
  "labTestResults": [...] // All lab test results for the patient
}
```

#### 3. Get Medical History Grouped by Visits
```http
GET /emr/medical-history/patient/{patientId}/by-visits

Response: 200 OK
List<VisitMedicalHistoryDTO>
[
  {
    "visitId": 123,
    "clinicalNotes": [...],
    "diagnosisNotes": [...],
    "prescriptions": [...],
    "vitalSigns": [...],
    "labTestResults": [...], // Lab results for this specific visit
    "admission": {...}
  },
  ...
]
```

#### 4. Get Medical History for Specific Visit
```http
GET /emr/medical-history/patient/{patientId}/visit/{visitId}

Response: 200 OK
VisitMedicalHistoryDTO
```

#### 5. Get Lab Test Results for Patient (from Medical History)
```http
GET /emr/medical-history/patient/{patientId}/lab-results

Response: 200 OK
List<LabTestResultDTO>
```

---

## Data Transfer Objects (DTOs)

### LabTestResultDTO
```typescript
interface LabTestResultDTO {
  id: number;
  patientId: number;
  visitId?: number;
  requestedBy?: string;
  testIds: number[];
  tests?: LabTestDTO[]; // Populated with test details
  results?: string;
  interpretation?: string;
  comments?: string;
  status: TestStatus;
  carriedOutBy?: string;
  createdAt: string; // ISO datetime string
  updatedAt?: string; // ISO datetime string
  completedAt?: string; // ISO datetime string
}
```

### CreateLabTestResultRequest
```typescript
interface CreateLabTestResultRequest {
  patientId: number; // Required
  visitId?: number; // Optional
  requestedBy?: string;
  testIds: number[]; // Required, at least one
  results?: string;
  interpretation?: string;
  comments?: string;
  carriedOutBy?: string;
}
```

### UpdateLabTestResultRequest
```typescript
interface UpdateLabTestResultRequest {
  results?: string;
  interpretation?: string;
  comments?: string;
  status?: TestStatus;
}
```

### LabTestDTO
```typescript
interface LabTestDTO {
  id: number;
  name: string;
  price: number; // BigDecimal
  category: TestCategory;
  sampleType: SampleType;
  description?: string;
  referenceRange?: string;
}
```

### MedicalHistoryDTO
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
  labTestResults: LabTestResultDTO[]; // All lab test results
}
```

### VisitMedicalHistoryDTO
```typescript
interface VisitMedicalHistoryDTO {
  visitId: number;
  clinicalNotes: NoteDTO[];
  diagnosisNotes: NoteDTO[];
  prescriptions: PrescriptionDTO[];
  vitalSigns: VitalSignsDTO[];
  labTestResults: LabTestResultDTO[]; // Lab results for this visit
  admission?: AdmissionDTO;
  
  // Helper method
  hasData(): boolean; // Returns true if any data exists
}
```

### NoteDTO (Referenced)
```typescript
interface NoteDTO {
  id: number;
  content: string;
  author: string;
  createdAt: string; // ISO datetime string
  updatedAt?: string; // ISO datetime string
  visitId?: number;
  medicalHistoryId: number;
}
```

### PrescriptionDTO (Referenced)
```typescript
interface PrescriptionDTO {
  id: number;
  prescriptionEntries: PrescriptionEntryDTO[];
  additionalInstructions?: string;
  prescriberId: number;
  medicalHistoryId: number;
  createdAt: string; // ISO datetime string
  updatedAt: string; // ISO datetime string
}
```

### VitalSignsDTO (Referenced)
```typescript
interface VitalSignsDTO {
  id: number;
  timeTaken: string; // ISO datetime string
  temperature?: number; // °C
  pulseRate?: number; // bpm
  respiratoryRate?: number; // breaths/min
  systolicBp?: number; // mmHg
  diastolicBp?: number; // mmHg
  oxygenSaturation?: number; // %
  bloodGlucose?: number; // mg/dL or mmol/L
  weight?: number; // kg
  height?: number; // cm
  bmi?: number; // kg/m²
  author: string;
  medicalHistoryId: number;
}
```

### AdmissionDTO (Referenced)
```typescript
interface AdmissionDTO {
  id: number;
  patientId: number;
  visitId?: number;
  patientNames: string;
  patientCode: string;
  wardId: number;
  wardName: string;
  medicalHistoryId: number;
  prescriptionChartDTO?: PrescriptionChartDTO;
  notes: NoteDTO[];
  admissionRecord?: NoteDTO;
  admissionDate: string; // ISO datetime string
  dischargeDate?: string; // ISO datetime string
}
```

---

## Enums

### TestStatus
```typescript
enum TestStatus {
  PENDING = "PENDING",
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED"
}
```

### TestCategory (Referenced)
```typescript
enum TestCategory {
  // Values depend on your implementation
  // Common values: CHEMISTRY, HEMATOLOGY, MICROBIOLOGY, etc.
}
```

### SampleType (Referenced)
```typescript
enum SampleType {
  // Values depend on your implementation
  // Common values: BLOOD, URINE, STOOL, SPUTUM, etc.
}
```

---

## Relationships

### LabTestResult ↔ MedicalHistory
- **Relationship Type:** Many-to-One (LabTestResult → MedicalHistory)
- **Bidirectional:** Yes
- **Cascade:** ALL (when lab test result is deleted, it's removed from medical history)
- **Auto-linking:** When a lab test result is created, it's automatically linked to the patient's medical history

### MedicalHistory ↔ Patient
- **Relationship Type:** One-to-One
- **Cascade:** ALL

### MedicalHistory ↔ Other Entities
- **Clinical Notes:** One-to-Many
- **Diagnosis Notes:** One-to-Many
- **Prescriptions:** One-to-Many
- **Vital Signs:** One-to-Many
- **Admissions:** One-to-Many

---

## Service Layer Methods

### LabResultService

#### Methods
1. `createResult(CreateLabTestResultRequest request): LabTestResultDTO`
   - Creates a new lab test result
   - Automatically links to medical history
   - Validates test IDs exist

2. `updateResult(Long resultId, UpdateLabTestResultRequest request): LabTestResultDTO`
   - Updates an existing lab test result
   - Sets `completedAt` when status changes to COMPLETED

3. `getResultById(Long resultId): LabTestResultDTO`
   - Retrieves a single lab test result by ID
   - Populates test details from registry

4. `getResultsByPatientId(Long patientId): List<LabTestResultDTO>`
   - Gets all lab test results for a patient

5. `getResultsByVisitId(Long visitId): List<LabTestResultDTO>`
   - Gets all lab test results for a visit

6. `getResultsByStatus(TestStatus status): List<LabTestResultDTO>`
   - Gets all lab test results with a specific status

7. `getPendingResults(): List<LabTestResultDTO>`
   - Gets all pending and in-progress results

### MedicalHistoryService

#### Methods
1. `createMedicalHistory(MedicalHistoryDTO dto): MedicalHistoryDTO`
   - Creates a new medical history record

2. `getMedicalHistory(Long patientId): MedicalHistoryDTO`
   - Gets complete medical history for a patient
   - Includes all lab test results with full details

3. `getMedicalHistoryByVisits(Long patientId): List<VisitMedicalHistoryDTO>`
   - Gets medical history grouped by visits
   - Lab test results are grouped by visit ID
   - Returns sorted by visitId (most recent first)

4. `getLabTestResultsForPatient(Long patientId): List<LabTestResultDTO>`
   - Gets all lab test results for a patient from their medical history
   - Returns empty list if no medical history exists

---

## Repository Methods

### LabTestResultRepository
- `findByPatientId(Long patientId): List<LabTestResult>`
- `findByVisitId(Long visitId): List<LabTestResult>`
- `findByStatus(TestStatus status): List<LabTestResult>`
- `findPendingResults(List<TestStatus> statuses): List<LabTestResult>`
- `findByPatientIdAndStatus(Long patientId, TestStatus status): List<LabTestResult>`
- `findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate): List<LabTestResult>`
- `findByPatientIdAndCreatedAtBetween(Long patientId, LocalDateTime startDate, LocalDateTime endDate): List<LabTestResult>`
- `findByTestId(Long testId): List<LabTestResult>`

### MedicalHistoryRepository
- `findByPatient_Id(Long patientId): MedicalHistory`

---

## Important Notes for Frontend Integration

### 1. Lab Test Results in Medical History
- Lab test results are automatically linked to medical history when created
- When fetching medical history, lab test results include full test details (via `tests` array in DTO)
- Lab test results can be accessed through:
  - Medical history endpoint (all results)
  - Visit-grouped endpoint (grouped by visit)
  - Dedicated lab results endpoint

### 2. Status Management
- Lab test results start with `PENDING` status
- Status can be updated to `IN_PROGRESS`, `COMPLETED`, or `CANCELLED`
- When status changes to `COMPLETED`, `completedAt` timestamp is automatically set

### 3. Test Details Population
- The `tests` array in `LabTestResultDTO` is populated from the test registry
- This includes test name, category, sample type, price, etc.
- If a test ID doesn't exist, it's filtered out (won't cause an error)

### 4. Visit Grouping
- Lab test results are grouped by `visitId` in the visit-grouped endpoint
- Results without a `visitId` won't appear in visit-grouped results
- Visit-grouped results are sorted by visitId (most recent first)

### 5. Date/Time Format
- All datetime fields are returned as ISO 8601 strings
- Format: `"2026-01-24T10:00:00"` or with timezone

### 6. Error Handling
- All endpoints return appropriate HTTP status codes
- 200 OK for successful requests
- 201 Created for resource creation
- 400 Bad Request for validation errors
- 404 Not Found when resource doesn't exist

### 7. Validation
- `patientId` is required when creating lab test results
- At least one `testId` is required
- Test IDs are validated to exist before creation

---

## Example Frontend Integration Scenarios

### Scenario 1: Display Patient's Lab Results
```typescript
// Fetch all lab results for a patient
const response = await fetch(`/emr/medical-history/patient/${patientId}/lab-results`);
const labResults: LabTestResultDTO[] = await response.json();

// Display results grouped by status
const pending = labResults.filter(r => r.status === 'PENDING');
const completed = labResults.filter(r => r.status === 'COMPLETED');
```

### Scenario 2: Display Medical History with Lab Results
```typescript
// Fetch complete medical history
const response = await fetch(`/emr/medical-history/patient/${patientId}`);
const medicalHistory: MedicalHistoryDTO = await response.json();

// Access lab results
const labResults = medicalHistory.labTestResults;
```

### Scenario 3: Display Visit-Specific Lab Results
```typescript
// Fetch visit-grouped medical history
const response = await fetch(`/emr/medical-history/patient/${patientId}/by-visits`);
const visits: VisitMedicalHistoryDTO[] = await response.json();

// Get lab results for a specific visit
const visit = visits.find(v => v.visitId === visitId);
const labResultsForVisit = visit?.labTestResults || [];
```

### Scenario 4: Create New Lab Test Result
```typescript
const createLabResult = async (patientId: number, visitId: number, testIds: number[]) => {
  const response = await fetch('/emr/lab/results', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      patientId,
      visitId,
      testIds,
      requestedBy: 'Dr. Smith',
      status: 'PENDING'
    })
  });
  return await response.json();
};
```

### Scenario 5: Update Lab Test Result Status
```typescript
const updateLabResultStatus = async (resultId: number, status: TestStatus) => {
  const response = await fetch(`/emr/lab/results/${resultId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ status })
  });
  return await response.json();
};
```

---

## Database Schema Notes

### Lab Test Results Table
- Table name: `lab_test_results`
- Join table for test IDs: `lab_test_result_test_ids`
- Foreign key to medical history: `medical_history_id`

### Medical History Table
- Table name: `medical_history`
- Foreign key to patient: `patient_id`

---

## End of Documentation

For questions or clarifications, refer to the source code in:
- `src/main/java/hospital/emr/lab/` (Lab module)
- `src/main/java/hospital/emr/patient/` (Medical History module)
