// header.js

// =========================
// Render Header
// =========================
export function renderHeader() {

    const headerDiv = document.getElementById("header");
    if (!headerDiv) return;

    // =========================
    // Root Page (Landing Page)
    // =========================
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");

        headerDiv.innerHTML = `
            <header class="header">
                <div class="logo-section">
                    <img src="../assets/images/logo/Logo.png" alt="Hospital CMS Logo" class="logo-img">
                    <span class="logo-title">Hospital CMS</span>
                </div>
            </header>
        `;
        return;
    }

    // =========================
    // Get Session Data
    // =========================
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // =========================
    // Session Validation
    // =========================
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    // =========================
    // Base Header
    // =========================
    let headerContent = `
        <header class="header">
            <div class="logo-section">
                <img src="../assets/images/logo/Logo.png" alt="Hospital CMS Logo" class="logo-img">
                <span class="logo-title">Hospital CMS</span>
            </div>
            <nav class="nav-actions">
    `;

    // =========================
    // Role-Based UI
    // =========================

    // ADMIN
    if (role === "admin") {
        headerContent += `
            <button id="addDocBtn" class="adminBtn">Add Doctor</button>
            <a href="#" id="logoutBtn">Logout</a>
        `;
    }

    // DOCTOR
    else if (role === "doctor") {
        headerContent += `
            <button id="doctorHome" class="adminBtn">Home</button>
            <a href="#" id="logoutBtn">Logout</a>
        `;
    }

    // PATIENT (NOT LOGGED IN)
    else if (role === "patient") {
        headerContent += `
            <button id="patientLogin" class="adminBtn">Login</button>
            <button id="patientSignup" class="adminBtn">Sign Up</button>
        `;
    }

    // LOGGED-IN PATIENT
    else if (role === "loggedPatient") {
        headerContent += `
            <button id="homeBtn" class="adminBtn">Home</button>
            <button id="appointmentsBtn" class="adminBtn">Appointments</button>
            <a href="#" id="logoutPatientBtn">Logout</a>
        `;
    }

    // DEFAULT (fallback)
    else {
        headerContent += `
            <button id="patientLogin" class="adminBtn">Login</button>
        `;
    }

    // Close header
    headerContent += `
            </nav>
        </header>
    `;

    // =========================
    // Render Header
    // =========================
    headerDiv.innerHTML = headerContent;

    // =========================
    // Attach Events
    // =========================
    attachHeaderButtonListeners(role);
}

// =========================
// Attach Event Listeners
// =========================
function attachHeaderButtonListeners(role) {

    // Admin - Add Doctor
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {
            openModal("addDoctor");
        });
    }

    // Doctor Home
    const doctorHome = document.getElementById("doctorHome");
    if (doctorHome) {
        doctorHome.addEventListener("click", () => {
            window.location.href = "/pages/doctorDashboard.html";
        });
    }

    // Patient Login
    const patientLogin = document.getElementById("patientLogin");
    if (patientLogin) {
        patientLogin.addEventListener("click", () => {
            openModal("patientLogin");
        });
    }

    // Patient Signup
    const patientSignup = document.getElementById("patientSignup");
    if (patientSignup) {
        patientSignup.addEventListener("click", () => {
            openModal("patientSignup");
        });
    }

    // Logged Patient Navigation
    const homeBtn = document.getElementById("homeBtn");
    if (homeBtn) {
        homeBtn.addEventListener("click", () => {
            window.location.href = "/pages/loggedPatientDashboard.html";
        });
    }

    const appointmentsBtn = document.getElementById("appointmentsBtn");
    if (appointmentsBtn) {
        appointmentsBtn.addEventListener("click", () => {
            window.location.href = "/pages/patientAppointments.html";
        });
    }

    // Logout (Admin/Doctor)
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }

    // Logout Patient
    const logoutPatientBtn = document.getElementById("logoutPatientBtn");
    if (logoutPatientBtn) {
        logoutPatientBtn.addEventListener("click", logoutPatient);
    }
}

// =========================
// Logout Functions
// =========================
function logout() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

function logoutPatient() {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = "/";
}

// =========================
// Initialize Header
// =========================
renderHeader();
