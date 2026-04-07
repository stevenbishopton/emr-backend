package hospital.emr.patient.controllers;

import hospital.emr.common.exceptions.GlobalExceptionHandler;
import hospital.emr.patient.dtos.PatientDTO;
import hospital.emr.patient.repos.PatientRepository;
import hospital.emr.patient.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing patients in the EMR system.
 * Provides endpoints for creating, retrieving, updating, and deleting patient records.
 */
@RestController
@RequestMapping(
    value = "/emr/patients",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Patient Management", description = "APIs for managing patient records")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final PatientRepository patientRepository;

    @Operation(
        summary = "Create a new patient",
        description = "Creates a new patient record with the provided details.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Patient created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Patient with the same identifier already exists",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> createPatient(
            @Parameter(description = "Patient details to create", required = true)
            @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Update an existing patient",
        description = "Updates the details of an existing patient identified by ID.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Patient updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })


    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PatientDTO> updatePatient(
            @Parameter(description = "ID of the patient to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated patient details", required = true)
            @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @Operation(
        summary = "Delete a patient",
        description = "Deletes a patient record by ID.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Patient deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID of the patient to delete", required = true)
            @PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all patients",
        description = "Retrieves a paginated list of all patients with optional filtering and sorting.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of patients",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Page.class)
            )
        )
    })
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<PatientDTO> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }

    @Operation(
        summary = "Find patient by phone number",
        description = "Retrieves a patient record by their phone number.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Patient found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found with the given phone number",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })


    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<PatientDTO> findPatientByPhoneNumber(
            @Parameter(description = "Phone number of the patient to retrieve", required = true)
            @PathVariable String phoneNumber) {
        PatientDTO patient = patientService.findPatientByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(patient);
    }

    @Operation(
        summary = "Find patient by patient code",
        description = "Retrieves a patient record by their unique patient code.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Patient found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found with the given code",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })


    @GetMapping("/code/{code}")
    public ResponseEntity<PatientDTO> findPatientByCode(
            @Parameter(description = "Unique patient code", required = true)
            @PathVariable String code) {
        PatientDTO patient = patientService.findPatientByCode(code);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDTO> findPatientById(@PathVariable Long patientId){
        PatientDTO patient = patientService.findPatientById(patientId);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/name/{patientId}")
    public ResponseEntity<String> findPatientNameById(@PathVariable Long patientId){
        String patientName = patientService.findPatientNameById(patientId);
        return ResponseEntity.ok(patientName);
    }

    // --- SEARCH ENDPOINTS ---

    @Operation(
        summary = "Search patients by name",
        description = "Returns a list of patients whose names contain the search term (case-insensitive).",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved matching patients",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        )
    })
    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatientsByName(
            @Parameter(description = "Search term to match against patient names", required = true)
            @RequestParam("searchTerm") String searchTerm) {
        List<PatientDTO> results = patientService.searchPatientsByName(searchTerm);
        return ResponseEntity.ok(results);
    }

    @Operation(
        summary = "Search patient by code",
        description = "Returns a single patient matching the provided code.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Patient found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found with the given code",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })
    @GetMapping("/search/code")
    public ResponseEntity<PatientDTO> searchPatientByCode(
            @Parameter(description = "Patient code to search", required = true)
            @RequestParam("code") String code) {
        try {
            PatientDTO patient = patientService.searchPatientByCode(code);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Search patient by phone number",
        description = "Returns a single patient matching the provided phone number.",
        tags = {"Patient Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Patient found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found with the given phone number",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })
    @GetMapping("/search/phone")
    public ResponseEntity<PatientDTO> searchPatientByPhoneNumber(
            @Parameter(description = "Phone number to search", required = true)
            @RequestParam("phoneNumber") String phoneNumber) {
        try {
            PatientDTO patient = patientService.searchPatientByPhoneNumber(phoneNumber);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- HMO ENDPOINTS ---

    @Operation(
        summary = "Find patients by health insurance status",
        description = "Returns a list of patients filtered by their health insurance status.",
        tags = {"Patient Management", "HMO"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved patients",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        )
    })
    @GetMapping("/hmo/insured/{isHealthInsured}")
    public ResponseEntity<List<PatientDTO>> findPatientsByHealthInsuranceStatus(
            @Parameter(description = "Health insurance status", required = true)
            @PathVariable Boolean isHealthInsured) {
        List<PatientDTO> patients = patientService.findPatientsByHealthInsuranceStatus(isHealthInsured);
        return ResponseEntity.ok(patients);
    }

    @Operation(
        summary = "Find patient by HMO policy number",
        description = "Returns a single patient matching the provided HMO policy number.",
        tags = {"Patient Management", "HMO"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Patient found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found with the given HMO policy number",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
        )
    })
    @GetMapping("/hmo/policy/{hmoPolicyNumber}")
    public ResponseEntity<PatientDTO> findPatientByHmoPolicyNumber(
            @Parameter(description = "HMO policy number to search", required = true)
            @PathVariable String hmoPolicyNumber) {
        try {
            PatientDTO patient = patientService.findPatientByHmoPolicyNumber(hmoPolicyNumber);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Search patients by HMO name",
        description = "Returns a list of patients whose HMO name contains the search term (case-insensitive).",
        tags = {"Patient Management", "HMO"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved matching patients",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        )
    })
    @GetMapping("/hmo/search")
    public ResponseEntity<List<PatientDTO>> searchPatientsByHmoName(
            @Parameter(description = "HMO name search term", required = true)
            @RequestParam("hmoName") String hmoName) {
        List<PatientDTO> results = patientService.searchPatientsByHmoName(hmoName);
        return ResponseEntity.ok(results);
    }

    @Operation(
        summary = "Find patients with HMO insurance",
        description = "Returns a list of patients who have HMO insurance (isHealthInsured=true and HMO name is set).",
        tags = {"Patient Management", "HMO"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved patients with HMO",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PatientDTO.class)
            )
        )
    })
    @GetMapping("/hmo/insured")
    public ResponseEntity<List<PatientDTO>> findPatientsWithHmo() {
        List<PatientDTO> patients = patientService.findPatientsWithHmo();
        return ResponseEntity.ok(patients);
    }

    @Operation(
        summary = "Check if HMO policy number exists",
        description = "Returns a boolean indicating whether the given HMO policy number is already in use.",
        tags = {"Patient Management", "HMO"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully checked policy number"
        )
    })
    @GetMapping("/hmo/policy/check")
    public ResponseEntity<Boolean> checkHmoPolicyNumberExists(
            @Parameter(description = "HMO policy number to check", required = true)
            @RequestParam("hmoPolicyNumber") String hmoPolicyNumber) {
        boolean exists = patientService.checkHmoPolicyNumberExists(hmoPolicyNumber);
        return ResponseEntity.ok(exists);
    }

//    @GetMapping("/search/names/{names}")
//    public ResponseEntity<PatientDTO> findPatientByNames(@PathVariable String names) {
//        PatientDTO patient = patientService.findPatientByNames(names);
//        return new ResponseEntity<>(patient, HttpStatus.OK);
//    }
}