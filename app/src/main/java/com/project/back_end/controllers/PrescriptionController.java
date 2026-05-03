package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import com.project.back_end.services.AppointmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;
    private final AppointmentService appointmentService;

    // Constructor Injection
    public PrescriptionController(PrescriptionService prescriptionService,
                                 Service service,
                                 AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    // 3. Save Prescription (Doctor only)
    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescription(
            @PathVariable String token,
            @RequestBody Prescription prescription
    ) {

        // Validate doctor token
        String validation = service.validateToken(token, "doctor");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        // Update appointment status (e.g., 1 = completed / prescribed)
        appointmentService.changeStatus(prescription.getAppointmentId(), 1);

        // Save prescription
        return prescriptionService.savePrescription(prescription);
    }

    // 4. Get Prescription by Appointment ID (Doctor only)
    @GetMapping("/{token}/{appointmentId}")
    public ResponseEntity<?> getPrescription(
            @PathVariable String token,
            @PathVariable Long appointmentId
    ) {

        // Validate doctor token
        String validation = service.validateToken(token, "doctor");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}