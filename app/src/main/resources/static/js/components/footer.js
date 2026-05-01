// footer.js

// =========================
// Render Footer Function
// =========================
export function renderFooter() {

    // Select footer placeholder
    const footer = document.getElementById("footer");

    if (!footer) {
        console.warn("Footer element not found");
        return;
    }

    // Inject footer HTML
    footer.innerHTML = `
        <footer class="footer">
            <div class="footer-container">

                <!-- Logo + Copyright -->
                <div class="footer-logo">
                    <img src="../assets/images/logo/Logo.png" alt="Hospital CMS Logo">
                    <p>© Copyright 2025. All Rights Reserved by Hospital CMS.</p>
                </div>

                <!-- Links Section -->
                <div class="footer-links">

                    <!-- Company -->
                    <div class="footer-column">
                        <h4>Company</h4>
                        <a href="#">About</a>
                        <a href="#">Careers</a>
                        <a href="#">Press</a>
                    </div>

                    <!-- Support -->
                    <div class="footer-column">
                        <h4>Support</h4>
                        <a href="#">Account</a>
                        <a href="#">Help Center</a>
                        <a href="#">Contact Us</a>
                    </div>

                    <!-- Legals -->
                    <div class="footer-column">
                        <h4>Legals</h4>
                        <a href="#">Terms & Conditions</a>
                        <a href="#">Privacy Policy</a>
                        <a href="#">Licensing</a>
                    </div>

                </div>

            </div>
        </footer>
    `;
}

// =========================
// Auto-render on load
// =========================
renderFooter();
