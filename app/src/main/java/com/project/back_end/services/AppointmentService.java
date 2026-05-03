package com.project.back_end.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // (Optional) shared services if you have them
    private final Service service;          // common service (token validation, etc.)
    private final TokenService tokenService;

    // Constructor Injection
    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              Service service,
                              TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.service = service;
        this.tokenService = tokenService;
    }

    // 1. Book Appointment
    @Transactional
    public int bookAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime) {
        try {
            Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
            Optional<Patient> patientOpt = patientRepository.findById(patientId);

            if (doctorOpt.isEmpty() || patientOpt.isEmpty()) return 0;

            // Check overlapping appointments (simple rule)
            LocalDateTime start = appointmentTime;
            LocalDateTime end = appointmentTime.plusHours(1);

            List<Appointment> existing = appointmentRepository
                    .findByDoctorIdAndAppointmentTimeBetween(doctorId, start.minusHours(1), end);

            if (!existing.isEmpty()) return 0;

            Appointment appointment = new Appointment();
            appointment.setDoctor(doctorOpt.get());
            appointment.setPatient(patientOpt.get());
            appointment.setAppointmentTime(appointmentTime);
            appointment.setStatus(0);

            appointmentRepository.save(appointment);
            return 1;

        } catch (Exception e) {
            return 0;
        }
    }

    // 2. Update Appointment
    @Transactional
    public String updateAppointment(Long appointmentId, Long patientId, LocalDateTime newTime) {

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) return "Appointment not found";

        Appointment appointment = appointmentOpt.get();

        // Validate ownership
        if (!appointment.getPatient().getId().equals(patientId)) {
            return "Unauthorized action";
        }

        // Prevent update if already completed
        if (appointment.getStatus() == 1) {
            return "Cannot update completed appointment";
        }

        // Check doctor availability
        Long doctorId = appointment.getDoctor().getId();
        List<Appointment> conflicts = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        newTime.minusHours(1),
                        newTime.plusHours(1)
                );

        if (!conflicts.isEmpty()) return "Doctor not available at this time";

        appointment.setAppointmentTime(newTime);
        appointmentRepository.save(appointment);

        return "Appointment updated successfully";
    }

    // 3. Cancel Appointment
    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {

        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) return "Appointment not found";

        Appointment appointment = appointmentOpt.get();

        if (!appointment.getPatient().getId().equals(patientId)) {
            return "Unauthorized action";
        }

        appointmentRepository.delete(appointment);
        return "Appointment cancelled successfully";
    }

    // 4. Get Appointments (Doctor view)
    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(Long doctorId, LocalDateTime start, LocalDateTime end, String patientName) {

        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository
                    .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                            doctorId, patientName, start, end
                    );
        }

        return appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
    }

    // 5. Change Status
    @Transactional
    public void changeStatus(Long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }
}