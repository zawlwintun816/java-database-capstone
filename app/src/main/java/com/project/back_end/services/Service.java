package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    private final DoctorService doctorService;
    private final PatientService patientService;

    // Constructor Injection
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   AppointmentRepository appointmentRepository,
                   DoctorService doctorService,
                   PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 3. Validate Token
    public String validateToken(String token, String role) {
        try {
            boolean valid = tokenService.validateToken(token, role);
            return valid ? "" : "Invalid token";
        } catch (Exception e) {
            return "Invalid token";
        }
    }

    // 4. Validate Admin Login
    public ResponseEntity<Map<String, Object>> validateAdmin(String username, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            Admin admin = adminRepository.findByUsername(username);

            if (admin == null) {
                response.put("error", "Admin not found");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            if (!admin.getPassword().equals(password)) {
                response.put("error", "Invalid password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            String token = tokenService.generateToken(username, "admin");

            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", "Server error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 5. Filter Doctors
    public List<Doctor> filterDoctor(String name, String specialty, String time) {

        if ((name == null || name.isEmpty()) &&
            (specialty == null || specialty.isEmpty()) &&
            (time == null || time.isEmpty())) {

            return doctorService.getDoctors();
        }

        if (name != null && !name.isEmpty() &&
            specialty != null && !specialty.isEmpty() &&
            time != null && !time.isEmpty()) {

            return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        }

        if (name != null && !name.isEmpty() &&
            specialty != null && !specialty.isEmpty()) {

            return doctorService.filterDoctorByNameAndSpecility(name, specialty);
        }

        if (name != null && !name.isEmpty() &&
            time != null && !time.isEmpty()) {

            return doctorService.filterDoctorByNameAndTime(name, time);
        }

        if (specialty != null && !specialty.isEmpty() &&
            time != null && !time.isEmpty()) {

            return doctorService.filterDoctorByTimeAndSpecility(specialty, time);
        }

        if (name != null && !name.isEmpty()) {
            return doctorService.findDoctorByName(name);
        }

        if (specialty != null && !specialty.isEmpty()) {
            return doctorService.filterDoctorBySpecility(specialty);
        }

        return doctorService.filterDoctorsByTime(time);
    }

    // 6. Validate Appointment
    public int validateAppointment(Long doctorId, LocalDateTime appointmentTime) {

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return -1;

        Doctor doctor = doctorOpt.get();

        LocalDate date = appointmentTime.toLocalDate();
        List<String> availableSlots =
                doctorService.getDoctorAvailability(doctorId, date);

        String requestedTime = appointmentTime.toLocalTime().toString();

        for (String slot : availableSlots) {
            String start = slot.split(" - ")[0];
            if (start.equals(requestedTime)) {
                return 1;
            }
        }

        return 0;
    }

    // 7. Validate Patient (registration)
    public boolean validatePatient(Patient patient) {
        Patient existing =
                patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());

        return existing == null;
    }

    // 8. Validate Patient Login
    public ResponseEntity<Map<String, Object>> validatePatientLogin(String email, String password) {

        Map<String, Object> response = new HashMap<>();

        try {
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                response.put("error", "Patient not found");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            if (!patient.getPassword().equals(password)) {
                response.put("error", "Invalid password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            String token = tokenService.generateToken(email, "patient");

            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", "Server error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 9. Filter Patient Appointments
    public Object filterPatient(String token, String condition, String doctorName) {

        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) return Collections.emptyList();

            Long patientId = patient.getId();

            if (condition != null && doctorName != null) {
                return patientService.filterByDoctorAndCondition(patientId, doctorName, condition);
            }

            if (condition != null) {
                return patientService.filterByCondition(patientId, condition);
            }

            if (doctorName != null) {
                return patientService.filterByDoctor(patientId, doctorName);
            }

            return patientService.getPatientAppointment(patientId);

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}