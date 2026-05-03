package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    // Constructor Injection
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // 3. Get Doctor Availability
    @GetMapping("/availability/{user}/{token}/{doctorId}/{date}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable String token,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {

        String validation = service.validateToken(token, user);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        List<String> availability = doctorService.getDoctorAvailability(doctorId, date);
        return ResponseEntity.ok(availability);
    }

    // 4. Get All Doctors
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getDoctor() {
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctorService.getDoctors());
        return ResponseEntity.ok(response);
    }

    // 5. Save Doctor (Admin only)
    @PostMapping("/save/{token}")
    public ResponseEntity<?> saveDoctor(
            @PathVariable String token,
            @RequestBody Doctor doctor
    ) {

        String validation = service.validateToken(token, "admin");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        int result = doctorService.saveDoctor(doctor);

        if (result == -1) {
            return ResponseEntity.status(409).body("Doctor already exists");
        }

        if (result == 1) {
            return ResponseEntity.ok("Doctor saved successfully");
        }

        return ResponseEntity.status(500).body("Error saving doctor");
    }

    // 6. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@RequestBody Login login) {
        String result = doctorService.validateDoctor(login.getEmail(), login.getPassword());

        if (result.startsWith("Invalid")) {
            return ResponseEntity.status(401).body(result);
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", result);

        return ResponseEntity.ok(response);
    }

    // 7. Update Doctor (Admin only)
    @PutMapping("/update/{token}/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable String token,
            @PathVariable Long id,
            @RequestBody Doctor doctor
    ) {

        String validation = service.validateToken(token, "admin");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        int result = doctorService.updateDoctor(id, doctor);

        if (result == -1) {
            return ResponseEntity.status(404).body("Doctor not found");
        }

        if (result == 1) {
            return ResponseEntity.ok("Doctor updated successfully");
        }

        return ResponseEntity.status(500).body("Error updating doctor");
    }

    // 8. Delete Doctor (Admin only)
    @DeleteMapping("/delete/{token}/{doctorId}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable String token,
            @PathVariable Long doctorId
    ) {

        String validation = service.validateToken(token, "admin");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        int result = doctorService.deleteDoctor(doctorId);

        if (result == -1) {
            return ResponseEntity.status(404).body("Doctor not found");
        }

        if (result == 1) {
            return ResponseEntity.ok("Doctor deleted successfully");
        }

        return ResponseEntity.status(500).body("Error deleting doctor");
    }

    // 9. Filter Doctors
    @GetMapping("/filter")
    public ResponseEntity<?> filterDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String specialty
    ) {

        List<Doctor> doctors = service.filterDoctor(name, specialty, time);
        return ResponseEntity.ok(doctors);
    }
}