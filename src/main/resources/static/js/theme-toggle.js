// js/theme-toggle.js
document.addEventListener("DOMContentLoaded", function () {
  const themeBtn = document.getElementById("themeBtn");
  const html = document.documentElement;

  // Leggi il tema salvato o fallback a "light"
  const savedTheme = localStorage.getItem("theme") || "light";
  html.setAttribute("data-bs-theme", savedTheme);

  // Imposta l'emoji iniziale
  if (themeBtn) {
    themeBtn.textContent = savedTheme === "light" ? "🌙" : "☀️";

    themeBtn.addEventListener("click", () => {
      const current = html.getAttribute("data-bs-theme");
      const newTheme = current === "light" ? "dark" : "light";
      html.setAttribute("data-bs-theme", newTheme);
      localStorage.setItem("theme", newTheme);
      themeBtn.textContent = newTheme === "light" ? "🌙" : "☀️";
    });
  }
});