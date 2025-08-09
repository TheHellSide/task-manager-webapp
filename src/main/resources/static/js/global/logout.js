document.getElementById('logoutBtn').addEventListener('click', async () => {
    const userData = JSON.parse(localStorage.getItem('loggedUser'));

    if (!userData || !userData.token || !userData.id) {
        localStorage.removeItem('loggedUser');
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/v1/tokens/user/${userData.id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + userData.token
            }
        });

        if (!response.ok) {
            console.warn("Token cleanup failed:", await response.text());
        }
    }
    catch (err) {
        console.error("Logout failed:", err.message);
    }

    localStorage.removeItem('loggedUser');
    window.location.href = 'login.html';
});