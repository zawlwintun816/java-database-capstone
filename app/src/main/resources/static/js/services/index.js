// services/index.js

import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";

// =========================
// API Endpoints
// =========================
const ADMIN_API = API_BASE_URL + "/admin";
const DOCTOR_API = API_BASE_URL + "/doctor/login";

// =========================
// Button Event Binding
// =========================
window.onload = function () {

    const adminBtn = document.getElementById("adminLogin");
    if (adminBtn) {
        adminBtn.addEventListener("click", () => {
            openModal("adminLogin");
        });
    }

    const doctorBtn = document.getElementById("doctorLogin");
    if (doctorBtn) {
        doctorBtn.addEventListener("click", () => {
            openModal("doctorLogin");
        });
    }
};

// =========================
// Admin Login Handler
// =========================
window.adminLoginHandler = async function () {
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    const admin = { username, password };

    try {
        const res = await fetch(ADMIN_API, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(admin)
        });

        if (res.ok) {
            const data = await res.json();
            localStorage.setItem("token", data.token);
            selectRole("admin");
        } else {
            alert("Invalid credentials!");
        }
    } catch (err) {
        console.error(err);
        alert("Login failed. Try again.");
    }
};

// =========================
// Doctor Login Handler
// =========================
window.doctorLoginHandler = async function () {
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    const doctor = { email, password };

    try {
        const res = await fetch(DOCTOR_API, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(doctor)
        });

        if (res.ok) {
            const data = await res.json();
            localStorage.setItem("token", data.token);
            selectRole("doctor");
        } else {
            alert("Invalid credentials!");
        }
    } catch (err) {
        console.error(err);
        alert("Login failed.");
    }
};