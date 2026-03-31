document.addEventListener("DOMContentLoaded", function () {
    const themeBtn = document.getElementById("themeBtn");
    const html = document.documentElement;

    // PATH DELLE IMMAGINI
    const lightIcon = "../images/logo_light.svg";
    const darkIcon  = "../images/logo_dark.svg";

    // READ THE SAVED THEME OR SET IT "light"
    const savedTheme = localStorage.getItem("theme") || "light";
    html.setAttribute("data-bs-theme", savedTheme);

    // SET THE ICON
    if (themeBtn) {
        const img = document.createElement("img");
        img.src = savedTheme === "light" ? darkIcon : lightIcon;
        img.alt = "Theme Icon";
        img.style.width = "24px"; // regola se vuoi
        img.style.height = "24px";
        themeBtn.innerHTML = ""; // pulisco eventuale contenuto
        themeBtn.appendChild(img);

        themeBtn.addEventListener("click", () => {
            const current = html.getAttribute("data-bs-theme");
            const newTheme = current === "light" ? "dark" : "light";
            html.setAttribute("data-bs-theme", newTheme);
            localStorage.setItem("theme", newTheme);

            // Aggiorna immagine
            img.src = newTheme === "light" ? darkIcon : lightIcon;
        });
    }
});