package hospital.emr.radiograph.controllers;

import hospital.emr.radiograph.dtos.RadiographCatalogDTO;
import hospital.emr.radiograph.services.RadiographCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/emr/radiographs/catalog")
@RequiredArgsConstructor
@Slf4j
public class RadiographCatalogController {

    private final RadiographCatalogService catalogService;

    @GetMapping
    public ResponseEntity<List<RadiographCatalogDTO>> getAllCatalogItems() {
        log.debug("GET /emr/radiographs/catalog - Fetching all catalog items");
        List<RadiographCatalogDTO> items = catalogService.getAllCatalogItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/active")
    public ResponseEntity<List<RadiographCatalogDTO>> getActiveCatalogItems() {
        log.debug("GET /emr/radiographs/catalog/active - Fetching active catalog items");
        List<RadiographCatalogDTO> items = catalogService.getActiveCatalogItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RadiographCatalogDTO> getCatalogItemById(@PathVariable Long id) {
        log.debug("GET /emr/radiographs/catalog/{} - Fetching catalog item", id);
        RadiographCatalogDTO item = catalogService.getCatalogItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<RadiographCatalogDTO>> getCatalogByType(@PathVariable String type) {
        log.debug("GET /emr/radiographs/catalog/type/{} - Fetching catalog items by type", type);
        try {
            List<RadiographCatalogDTO> items = catalogService.getCatalogByType(
                hospital.emr.radiograph.enums.RadiographType.valueOf(type.toUpperCase())
            );
            return ResponseEntity.ok(items);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER')")
    public ResponseEntity<RadiographCatalogDTO> createCatalogItem(
            @Valid @RequestBody RadiographCatalogDTO dto) {
        log.info("POST /emr/radiographs/catalog - Creating catalog item: {}", dto.getName());
        try {
            RadiographCatalogDTO created = catalogService.createCatalogItem(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error creating catalog item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER')")
    public ResponseEntity<RadiographCatalogDTO> updateCatalogItem(
            @PathVariable Long id,
            @Valid @RequestBody RadiographCatalogDTO dto) {
        log.info("PUT /emr/radiographs/catalog/{} - Updating catalog item", id);
        try {
            RadiographCatalogDTO updated = catalogService.updateCatalogItem(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Error updating catalog item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCatalogItem(@PathVariable Long id) {
        log.info("DELETE /emr/radiographs/catalog/{} - Deleting catalog item", id);
        try {
            catalogService.deleteCatalogItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting catalog item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER')")
    public ResponseEntity<Void> activateCatalogItem(@PathVariable Long id) {
        log.info("POST /emr/radiographs/catalog/{}/activate - Activating catalog item", id);
        try {
            catalogService.activateCatalogItem(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error activating catalog item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER')")
    public ResponseEntity<Void> deactivateCatalogItem(@PathVariable Long id) {
        log.info("POST /emr/radiographs/catalog/{}/deactivate - Deactivating catalog item", id);
        try {
            catalogService.deactivateCatalogItem(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Error deactivating catalog item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> checkNameExists(@PathVariable String name) {
        log.debug("GET /emr/radiographs/catalog/exists/name/{} - Checking if name exists", name);
        boolean exists = catalogService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/id/{id}")
    public ResponseEntity<Boolean> checkIdExists(@PathVariable Long id) {
        log.debug("GET /emr/radiographs/catalog/exists/id/{} - Checking if id exists", id);
        boolean exists = catalogService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
