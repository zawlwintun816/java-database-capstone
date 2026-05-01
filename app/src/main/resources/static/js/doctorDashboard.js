// doctorDashboard.js

// =========================
// Imports
// =========================
import { getAllAppointments } from "../services/appointmentServices.js";
import { createPatientRow } from "../components/patientRow.js";

// =========================
// DOM Elements
// =========================
const tableBody = document.getElementById("appointmentTableBody");
const searchInput = document.getElementById("searchPatient");
const todayBtn = document.getElementById("todayBtn");
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

// Search by patient name
if (searchInput) {
    searchInput.addEventListener("input", () => {
        const value = searchInput.value.trim();
        patientName = value !== "" ? value : null;
        loadAppointments();
    });
}

// Today button
if (todayBtn) {
    todayBtn.addEventListener("click", () => {
        selectedDate = new Date().toISOString().split("T")[0];

        if (datePicker) {
            datePicker.value = selectedDate;
        }

        loadAppointments();
    });
}

// Date picker
if (datePicker) {
    datePicker.addEventListener("change", () => {
        selectedDate = datePicker.value;
        loadAppointments();
    });
}

// =========================
// Load Appointments
// =========================
async function loadAppointments() {

    try {
        const appointments = await getAllAppointments(selectedDate, patientName, token);

        // Clear existing rows
        tableBody.innerHTML = "";

        // No data case
        if (!appointments || appointments.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="noPatientRecord">
                        No Appointments found for today.
                    </td>
                </tr>
            `;
            return;
        }

        // Render rows
        appointments.forEach(app => {

            const patient = {
                id: app.patientId,
                name: app.patientName,
                phone: app.patientPhone,
                email: app.patientEmail
            };

            const row = createPatientRow(app, patient);
            tableBody.appendChild(row);
        });

    } catch (err) {
        console.error(err);

        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="noPatientRecord">
                    Error loading appointments. Try again later.
                </td>
            </tr>
        `;
    }
}

// =========================
// Initial Load
// =========================
document.addEventListener("DOMContentLoaded", () => {
    renderContent(); // assumes layout setup
    loadAppointments();
});
