// doctorDashboard.js

import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

// =========================
// DOM Elements
// =========================
const tableBody = document.getElementById("patientTableBody");
const searchInput = document.getElementById("searchBar");
const todayBtn = document.getElementById("todayButton");
const datePicker = document.getElementById("datePicker");

// =========================
// State
// =========================
let selectedDate = new Date().toISOString().split("T")[0];
let patientName = null;
const token = localStorage.getItem("token");

// =========================
// Event Listeners
// =========================
searchInput?.addEventListener("input", () => {
    const value = searchInput.value.trim();
    patientName = value !== "" ? value : null;
    loadAppointments();
});

todayBtn?.addEventListener("click", () => {
    selectedDate = new Date().toISOString().split("T")[0];
    if (datePicker) datePicker.value = selectedDate;
    loadAppointments();
});

datePicker?.addEventListener("change", () => {
    selectedDate = datePicker.value;
    loadAppointments();
});

// =========================
// Load Appointments
// =========================
async function loadAppointments() {
    try {
        const appointments = await getAllAppointments(selectedDate, patientName, token);

        tableBody.innerHTML = "";

        if (!appointments || appointments.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="5">No Appointments found for today</td>
                </tr>
            `;
            return;
        }

        appointments.forEach(app => {
            const patient = {
                id: app.patientId,
                name: app.patientName,
                phone: app.patientPhone,
                email: app.patientEmail
            };

            tableBody.appendChild(createPatientRow(app, patient));
        });

    } catch (err) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5">Error loading appointments</td>
            </tr>
        `;
    }
}

// =========================
// Initial Load
// =========================
document.addEventListener("DOMContentLoaded", () => {
    renderContent();
    loadAppointments();
});