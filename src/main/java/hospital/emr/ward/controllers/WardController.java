// hospital.emr.ward.controllers.WardController.java
package hospital.emr.ward.controllers;

import hospital.emr.ward.dtos.WardDTO;
import hospital.emr.ward.services.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/wards")
@RequiredArgsConstructor
public class WardController {
    private final WardService wardService;

    @PostMapping
    public ResponseEntity<WardDTO> createWard(@RequestBody WardDTO wardDto) {
        WardDTO createdWard = wardService.createWard(wardDto);
        return new ResponseEntity<>(createdWard, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WardDTO>> getAllWards() {
        List<WardDTO> wards = wardService.getAllWards();
        return new ResponseEntity<>(wards, HttpStatus.OK);
    }
}
