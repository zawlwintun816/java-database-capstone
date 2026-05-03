// adminDashboard.js

import { openModal } from "../components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

// =========================
// DOM Elements
// =========================
const contentDiv = document.getElementById("content");

// =========================
// Event Binding
// =========================
document.getElementById("addDocBtn")?.addEventListener("click", () => {
    openModal("addDoctor");
});

document.getElementById("searchBar")?.addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime")?.addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty")?.addEventListener("change", filterDoctorsOnChange);

// =========================
// Initial Load
// =========================
document.addEventListener("DOMContentLoaded", loadDoctorCards);

// =========================
// Load Doctors
// =========================
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (err) {
        console.error(err);
    }
}

// =========================
// Filter Doctors
// =========================
async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar")?.value.trim() || null;
    const time = document.getElementById("filterTime")?.value || null;
    const specialty = document.getElementById("filterSpecialty")?.value || null;

    try {
        const doctors = await filterDoctors(name, time, specialty);

        if (doctors.length > 0) {
            renderDoctorCards(doctors);
        } else {
            contentDiv.innerHTML = "<p>No doctors found</p>";
        }
    } catch (err) {
        alert("Error filtering doctors");
    }
}

// =========================
// Render Cards
// =========================
function renderDoctorCards(doctors) {
    contentDiv.innerHTML = "";

    doctors.forEach(doc => {
        contentDiv.appendChild(createDoctorCard(doc));
    });
}

// =========================
// Add Doctor
// =========================
window.adminAddDoctor = async function () {

    const name = document.getElementById("docName").value;
    const email = document.getElementById("docEmail").value;
    const phone = document.getElementById("docPhone").value;
    const password = document.getElementById("docPassword").value;
    const specialty = document.getElementById("docSpecialty").value;
    const availableSlots = document.getElementById("docAvailable").value.split(",");

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Unauthorized");
        return;
    }

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

        if (res.success) {
            alert("Doctor added successfully");
            closeModal();
            loadDoctorCards();
        } else {
            alert(res.message);
        }
    } catch (err) {
        alert("Error adding doctor");
    }
};