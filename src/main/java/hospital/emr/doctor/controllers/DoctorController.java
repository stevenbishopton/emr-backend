//package hospital.emr.doctor.controllers;
//
//import hospital.emr.doctor.dtos.DoctorCreateDTO;
//import hospital.emr.doctor.dtos.DoctorDTO;
//import hospital.emr.doctor.mappers.DoctorMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/emr/doctors")
//public class DoctorController {
//
//    private final DoctorService doctorService;
//    private final DoctorMapper doctorMapper;
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
//        DoctorDTO doctor = doctorService.getDoctorById(id);
//        if (doctor != null) {
//            return ResponseEntity.ok(doctor);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    @PostMapping
//    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorCreateDTO doctorCreateDto) {
//        DoctorDTO createdDoctor = doctorService.createDoctor(doctorCreateDto);
//        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
//    }
//}