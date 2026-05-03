package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    // Constructor Injection
    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // 3. Get Appointments (Doctor)
    @GetMapping("/{token}/{date}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String token,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String patientName
    ) {

        String validation = service.validateToken(token, "doctor");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        // Assuming doctorId comes from token (you can adjust if needed)
        String email = service.extractEmailFromToken(token);
        Long doctorId = service.getDoctorIdByEmail(email);

        return ResponseEntity.ok(
                appointmentService.getAppointments(doctorId, start, end, patientName)
        );
    }

    // 4. Book Appointment (Patient)
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment
    ) {

        String validation = service.validateToken(token, "patient");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        int check = service.validateAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime()
        );

        if (check == -1) {
            return ResponseEntity.badRequest().body("Doctor not found");
        }

        if (check == 0) {
            return ResponseEntity.badRequest().body("Time slot not available");
        }

        int result = appointmentService.bookAppointment(
                appointment.getDoctor().getId(),
                appointment.getPatient().getId(),
                appointment.getAppointmentTime()
        );

        if (result == 1) {
            return ResponseEntity.ok("Appointment booked successfully");
        }

        return ResponseEntity.status(500).body("Failed to book appointment");
    }

    // 5. Update Appointment
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable String token,
            @RequestBody Appointment appointment
    ) {

        String validation = service.validateToken(token, "patient");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        String result = appointmentService.updateAppointment(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getAppointmentTime()
        );

        return ResponseEntity.ok(result);
    }

    // 6. Cancel Appointment
    @DeleteMapping("/cancel/{token}/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable String token,
            @PathVariable Long appointmentId,
            @RequestParam Long patientId
    ) {

        String validation = service.validateToken(token, "patient");
        if (!validation.isEmpty()) {
            return ResponseEntity.status(401).body(validation);
        }

        String result = appointmentService.cancelAppointment(appointmentId, patientId);
        return ResponseEntity.ok(result);
    }
}