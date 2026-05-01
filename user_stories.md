# User Stories – Patient Appointment Portal

---

## Admin User Stories

### 1. Login to Portal
**Title:**  
_As an Admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can enter username and password.
2. System validates credentials before granting access.
3. Admin is redirected to the dashboard upon successful login.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Handle invalid login attempts with error messages.

---

### 2. Logout from Portal
**Title:**  
_As an Admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. Admin can click logout button.
2. Session is terminated securely.
3. Admin is redirected to login page.

**Priority:** High  
**Story Points:** 2  
**Notes:**
- Ensure session timeout is implemented.

---

### 3. Add Doctor
**Title:**  
_As an Admin, I want to add doctors to the portal, so that they can provide services to patients._

**Acceptance Criteria:**
1. Admin can enter doctor details.
2. System validates required fields.
3. Doctor account is created successfully.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Email should be unique.

---

### 4. Delete Doctor
**Title:**  
_As an Admin, I want to delete a doctor’s profile, so that I can manage outdated or inactive accounts._

**Acceptance Criteria:**
1. Admin can select a doctor profile.
2. System asks for confirmation before deletion.
3. Doctor profile is removed from the system.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Prevent deletion if doctor has active appointments.

---

### 5. View Appointment Statistics
**Title:**  
_As an Admin, I want to run a stored procedure to get monthly appointment counts, so that I can track system usage._

**Acceptance Criteria:**
1. Admin can trigger stored procedure.
2. System retrieves appointment data from MySQL.
3. Results are displayed clearly.

**Priority:** Medium  
**Story Points:** 5  
**Notes:**
- Ensure correct database connection.

---

## Patient User Stories

### 1. View Doctors Without Login
**Title:**  
_As a Patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. Patient can access doctor list page.
2. Doctor details are visible.
3. No login is required.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Limit sensitive information.

---

### 2. Sign Up
**Title:**  
_As a Patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Patient can enter registration details.
2. Email must be unique.
3. Account is created successfully.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Add email validation.

---

### 3. Login
**Title:**  
_As a Patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Patient enters credentials.
2. System validates login.
3. Patient is redirected to dashboard.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Show error for invalid login.

---

### 4. Book Appointment
**Title:**  
_As a Patient, I want to book an appointment, so that I can consult with a doctor._

**Acceptance Criteria:**
1. Patient selects doctor and time slot.
2. Appointment duration is one hour.
3. Booking is confirmed and stored.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Prevent double booking.

---

### 5. View Upcoming Appointments
**Title:**  
_As a Patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Patient can see list of appointments.
2. Appointments include date and doctor info.
3. Only future appointments are shown.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Sort by nearest date.

---

## Doctor User Stories

### 1. Login
**Title:**  
_As a Doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Doctor enters credentials.
2. System validates login.
3. Doctor is redirected to dashboard.

**Priority:** High  
**Story Points:** 3  
**Notes:**
- Secure authentication required.

---

### 2. Logout
**Title:**  
_As a Doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. Doctor clicks logout.
2. Session is terminated.
3. Redirect to login page.

**Priority:** High  
**Story Points:** 2  
**Notes:**
- Clear session data.

---

### 3. View Appointment Calendar
**Title:**  
_As a Doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Doctor can access calendar view.
2. Appointments are displayed by date.
3. Upcoming appointments are highlighted.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Include filtering options.

---

### 4. Mark Unavailability
**Title:**  
_As a Doctor, I want to mark my unavailability, so that patients only see available slots._

**Acceptance Criteria:**
1. Doctor selects unavailable time.
2. System blocks those slots.
3. Patients cannot book during those times.

**Priority:** Medium  
**Story Points:** 5  
**Notes:**
- Prevent conflicts with existing bookings.

---

### 5. Update Profile
**Title:**  
_As a Doctor, I want to update my profile information, so that patients have accurate details._

**Acceptance Criteria:**
1. Doctor can edit profile fields.
2. Changes are saved successfully.
3. Updated info is visible to patients.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**
- Validate required fields.

---

### 6. View Patient Details
**Title:**  
_As a Doctor, I want to view patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. Doctor can access patient info.
2. Details are linked to appointments.
3. Data is displayed securely.

**Priority:** High  
**Story Points:** 5  
**Notes:**
- Ensure privacy and access control.
