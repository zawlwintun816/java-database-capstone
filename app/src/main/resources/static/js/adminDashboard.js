// adminDashboard.js

// =========================
// Imports
// =========================
import { getDoctors, filterDoctors, saveDoctor } from "../services/doctorServices.js";
import { createDoctorCard } from "../components/doctorCard.js";

// =========================
// DOM Elements
// =========================
const contentDiv = document.getElementById("doctorContainer");
const searchInput = document.getElementById("searchInput");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");
const addDoctorBtn = document.getElementById("addDocBtn");

// =========================
// Event Listeners
// =========================

// Add Doctor Button
if (addDoctorBtn) {
    addDoctorBtn.addEventListener("click", () => {
        openModal("addDoctor");
    });
}

// Filters
if (searchInput) searchInput.addEventListener("input", filterDoctorsOnChange);
if (timeFilter) timeFilter.addEventListener("change", filterDoctorsOnChange);
if (specialtyFilter) specialtyFilter.addEventListener("change", filterDoctorsOnChange);

// =========================
// Initial Load
// =========================
document.addEventListener("DOMContentLoaded", loadDoctorCards);

// =========================
// Load All Doctors
// =========================
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (err) {
        console.error("Error loading doctors:", err);
    }
}

// =========================
// Filter Doctors
// =========================
async function filterDoctorsOnChange() {
    const name = searchInput?.value.trim() || null;
    const time = timeFilter?.value || null;
    const specialty = specialtyFilter?.value || null;

    try {
        const doctors = await filterDoctors(name, time, specialty);

        if (doctors && doctors.length > 0) {
            renderDoctorCards(doctors);
        } else {
            contentDiv.innerHTML = `<p class="noPatientRecord">No doctors found with the given filters.</p>`;
        }
    } catch (err) {
        console.error(err);
        alert("Error filtering doctors.");
    }
}

// =========================
// Render Doctor Cards
// =========================
function renderDoctorCards(doctors) {
    contentDiv.innerHTML = "";

    doctors.forEach(doc => {
        const card = createDoctorCard(doc);
        contentDiv.appendChild(card);
    });
}

// =========================
// Add Doctor
// =========================
export async function adminAddDoctor() {

    // Collect form values
    const name = document.getElementById("docName")?.value.trim();
    const email = document.getElementById("docEmail")?.value.trim();
    const phone = document.getElementById("docPhone")?.value.trim();
    const password = document.getElementById("docPassword")?.value.trim();
    const specialty = document.getElementById("docSpecialty")?.value;
    const availableSlots = document.getElementById("docAvailable")?.value
        .split(",")
        .map(s => s.trim());

    // Token check
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Unauthorized. Please log in.");
        return;
    }

    // Build doctor object
    const doctor = {
        name,
        email,
        phone,
        password,
        specialization: specialty,
        availableSlots
    };

    try {
        const res = await saveDoctor(doctor, token);

        if (res?.success) {
            alert("Doctor added successfully.");

            // Close modal (assuming function exists)
            closeModal();

            // Reload list (better than full page reload)
            loadDoctorCards();

        } else {
            alert(res?.message || "Failed to add doctor.");
        }
    } catch (err) {
        console.error(err);
        alert("Error adding doctor.");
    }
}
