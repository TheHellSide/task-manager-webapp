document.getElementById('logoutBtn').addEventListener('click', async () => {
    try {
        const response = await fetch('http://localhost:8080/api/v1/user/out', {
            method: 'POST',  // POST
            credentials: 'include'
        });

        if (!response.ok) {
            console.warn("Token cleanup failed:", await response.text());
        }
    } catch (err) {
        console.error("Logout failed:", err.message);
    }

    localStorage.removeItem('loggedUser');
    window.location.href = 'login.html';
});