package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // Constructor Injection
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // 4. Get Doctor Availability
    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return Collections.emptyList();

        Doctor doctor = doctorOpt.get();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        Set<String> bookedSlots = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toSet());

        return doctor.getAvailableTimes().stream()
                .filter(slot -> !bookedSlots.contains(slot.split(" - ")[0]))
                .collect(Collectors.toList());
    }

    // 5. Save Doctor
    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null) return -1;

            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 6. Update Doctor
    @Transactional
    public int updateDoctor(Long id, Doctor updatedDoctor) {
        Optional<Doctor> existingOpt = doctorRepository.findById(id);
        if (existingOpt.isEmpty()) return -1;

        Doctor doctor = existingOpt.get();
        doctor.setName(updatedDoctor.getName());
        doctor.setEmail(updatedDoctor.getEmail());
        doctor.setSpecialty(updatedDoctor.getSpecialty());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setAvailableTimes(updatedDoctor.getAvailableTimes());

        doctorRepository.save(doctor);
        return 1;
    }

    // 7. Get All Doctors
    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // 8. Delete Doctor
    @Transactional
    public int deleteDoctor(Long doctorId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return -1;

        try {
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 9. Validate Doctor Login
    public String validateDoctor(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email);

        if (doctor == null) return "Invalid email";
        if (!doctor.getPassword().equals(password)) return "Invalid password";

        return tokenService.generateToken(doctor.getId(), "doctor");
    }

    // 10. Find Doctor by Name
    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, "");
    }

    // 11. Filter by Name + Specialty + Time
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String time) {
        List<Doctor> doctors = doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorByTime(doctors, time);
    }

    // 12. Filter by Time
    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String time) {
        return doctors.stream()
                .filter(d -> d.getAvailableTimes().stream().anyMatch(t -> isTimeMatch(t, time)))
                .collect(Collectors.toList());
    }

    // 13. Filter by Name + Time
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        return filterDoctorByTime(doctors, time);
    }

    // 14. Filter by Name + Specialty
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    // 15. Filter by Time + Specialty
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String time) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorByTime(doctors, time);
    }

    // 16. Filter by Specialty
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    // 17. Filter All by Time
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByTime(String time) {
        List<Doctor> doctors = doctorRepository.findAll();
        return filterDoctorByTime(doctors, time);
    }

    // Helper for AM/PM filtering
    private boolean isTimeMatch(String slot, String period) {
        try {
            String start = slot.split(" - ")[0];
            int hour = Integer.parseInt(start.split(":")[0]);

            if ("AM".equalsIgnoreCase(period)) return hour < 12;
            if ("PM".equalsIgnoreCase(period)) return hour >= 12;

        } catch (Exception ignored) {}
        return false;
    }
}