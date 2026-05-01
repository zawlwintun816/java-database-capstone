// doctorCard.js

// =========================
// Imports
// =========================
import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientProfile } from "../services/patientServices.js";

// =========================
// Create Doctor Card
// =========================
export function createDoctorCard(doctor) {

    // Main card container
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    // Get role + token
    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");

    // =========================
    // Doctor Info Container
    // =========================
    const info = document.createElement("div");
    info.classList.add("doctor-info");

    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent = `Specialization: ${doctor.specialization}`;

    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    const availability = document.createElement("p");
    availability.textContent = `Available: ${doctor.availableSlots?.join(", ") || "N/A"}`;

    info.append(name, specialization, email, availability);

    // =========================
    // Actions Container
    // =========================
    const actions = document.createElement("div");
    actions.classList.add("card-actions");

    // =========================
    // ADMIN ROLE
    // =========================
    if (role === "ADMIN") {
        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.classList.add("delete-btn");

        deleteBtn.addEventListener("click", async () => {
            if (!token) {
                alert("Unauthorized. Please log in.");
                return;
            }

            try {
                const res = await deleteDoctor(doctor.id, token);

                if (res?.success) {
                    alert("Doctor deleted successfully.");
                    card.remove();
                } else {
                    alert(res?.message || "Failed to delete doctor.");
                }
            } catch (err) {
                console.error(err);
                alert("Error deleting doctor.");
            }
        });

        actions.appendChild(deleteBtn);
    }

    // =========================
    // PATIENT NOT LOGGED IN
    // =========================
    else if (role !== "PATIENT") {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";

        bookBtn.addEventListener("click", () => {
            alert("Please log in as a patient to book an appointment.");
        });

        actions.appendChild(bookBtn);
    }

    // =========================
    // LOGGED-IN PATIENT
    // =========================
    else {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";

        bookBtn.addEventListener("click", async () => {
            if (!token) {
                window.location.href = "/login.html";
                return;
            }

            try {
                const patient = await getPatientProfile(token);

                if (!patient) {
                    alert("Failed to fetch patient data.");
                    return;
                }

                // Show overlay UI
                showBookingOverlay({
                    doctor,
                    patient
                });

            } catch (err) {
                console.error(err);
                alert("Error loading booking data.");
            }
        });

        actions.appendChild(bookBtn);
    }

    // =========================
    // Final Assembly
    // =========================
    card.append(info, actions);

    return card;
}
