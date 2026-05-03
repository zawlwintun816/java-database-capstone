package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // 1. Find doctor by email
    Doctor findByEmail(String email);

    // 2. Find doctors by name (LIKE, case-sensitive)
    List<Doctor> findByNameLike(String name);

    // 3. Find by name (contains, ignore case) AND specialty (ignore case)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    // 4. Find by specialty (ignore case)
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}