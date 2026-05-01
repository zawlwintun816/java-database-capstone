# Schema Design – Smart Clinic Management System

---

## MySQL Database Design

Structured and relational data such as patients, doctors, and appointments are stored in MySQL. This ensures strong consistency, relationships, and constraints.

---

### Table: patients
- id: INT, Primary Key, AUTO_INCREMENT  
- first_name: VARCHAR(100), NOT NULL  
- last_name: VARCHAR(100), NOT NULL  
- email: VARCHAR(150), UNIQUE, NOT NULL  
- phone: VARCHAR(20), UNIQUE, NOT NULL  
- date_of_birth: DATE, NOT NULL  
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP  

**Notes:**
- Email and phone are marked UNIQUE to prevent duplicate accounts.
- Patient history should be retained even if inactive.

---

### Table: doctors
- id: INT, Primary Key, AUTO_INCREMENT  
- first_name: VARCHAR(100), NOT NULL  
- last_name: VARCHAR(100), NOT NULL  
- email: VARCHAR(150), UNIQUE, NOT NULL  
- specialization: VARCHAR(100), NOT NULL  
- phone: VARCHAR(20), UNIQUE  
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP  

**Notes:**
- Specialization is required for patient filtering.
- Doctors should not be deleted if they have appointment history (use soft delete if needed).

---

### Table: appointments
- id: INT, Primary Key, AUTO_INCREMENT  
- patient_id: INT, NOT NULL, Foreign Key → patients(id)  
- doctor_id: INT, NOT NULL, Foreign Key → doctors(id)  
- appointment_time: DATETIME, NOT NULL  
- status: INT, DEFAULT 0  (0 = Scheduled, 1 = Completed, 2 = Cancelled)  
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP  

**Constraints:**
- FOREIGN KEY (patient_id) REFERENCES patients(id)  
- FOREIGN KEY (doctor_id) REFERENCES doctors(id)  

**Notes:**
- Prevent overlapping appointments for the same doctor (handled at application level).
- Appointments should remain even if patient account is deactivated.

---

### Table: admin
- id: INT, Primary Key, AUTO_INCREMENT  
- username: VARCHAR(100), UNIQUE, NOT NULL  
- password: VARCHAR(255), NOT NULL  
- role: VARCHAR(50), DEFAULT 'ADMIN'  
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP  

**Notes:**
- Password should be stored as a hashed value.
- Role field allows future role expansion.

---

### Table: clinic_locations (optional enhancement)
- id: INT, Primary Key, AUTO_INCREMENT  
- name: VARCHAR(150), NOT NULL  
- address: TEXT, NOT NULL  
- phone: VARCHAR(20)  

**Notes:**
- Useful for multi-branch clinics.
- Can be linked to doctors later if needed.

---

## MongoDB Collection Design

MongoDB is used for flexible, document-based data such as prescriptions, which may vary in structure and contain nested information.

---

### Collection: prescriptions

```json
{
  "_id": "ObjectId('6612abc123def456')",
  "appointmentId": 101,
  "patientId": 12,
  "doctorId": 5,
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Twice a day",
      "duration_days": 5
    },
    {
      "name": "Ibuprofen",
      "dosage": "200mg",
      "frequency": "Once a day",
      "duration_days": 3
    }
  ],
  "doctorNotes": "Patient should rest and stay hydrated.",
  "attachments": [
    {
      "type": "image",
      "url": "https://clinic.com/reports/report1.png"
    }
  ],
  "pharmacy": {
    "name": "City Pharmacy",
    "location": "Bangkok"
  },
  "createdAt": "2026-05-01T10:30:00Z"
}
