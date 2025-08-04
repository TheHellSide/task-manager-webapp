// js/theme-toggle.js

document.addEventListener("DOMContentLoaded", function () {
  const themeBtn = document.getElementById("themeBtn");
  if (!themeBtn) return;

  themeBtn.addEventListener("click", () => {
    const html = document.documentElement;
    const current = html.getAttribute("data-bs-theme");
    if (current === "light") {
      html.setAttribute("data-bs-theme", "dark");
      themeBtn.textContent = "☀️";
    } else {
      html.setAttribute("data-bs-theme", "light");
      themeBtn.textContent = "🌙";
    }
  });
});
