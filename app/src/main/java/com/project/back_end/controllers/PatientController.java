package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    // Constructor Injection
    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    // 3. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {

        String validation = service.validateToken(token, "patient");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        Patient patient = patientService.getPatientDetails(token);
        return ResponseEntity.ok(patient);
    }

    // 4. Create Patient
    @PostMapping("/register")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {

        boolean isValid = service.validatePatient(patient);
        if (!isValid) {
            return ResponseEntity.status(409).body("Patient already exists");
        }

        int result = patientService.createPatient(patient);

        if (result == 1) {
            return ResponseEntity.ok("Patient registered successfully");
        }

        return ResponseEntity.status(500).body("Error creating patient");
    }

    // 5. Patient Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        return service.validatePatientLogin(login.getEmail(), login.getPassword());
    }

    // 6. Get Patient Appointments
    @GetMapping("/appointments/{user}/{token}/{patientId}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable String user,
            @PathVariable String token,
            @PathVariable Long patientId
    ) {

        String validation = service.validateToken(token, user);
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        return ResponseEntity.ok(
                patientService.getPatientAppointment(patientId)
        );
    }

    // 7. Filter Patient Appointments
    @GetMapping("/appointments/filter/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String token,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String name
    ) {

        String validation = service.validateToken(token, "patient");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        Object result = service.filterPatient(token, condition, name);
        return ResponseEntity.ok(result);
    }
}