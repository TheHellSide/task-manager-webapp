document.getElementById('sessionBtn').addEventListener('click', async () => {
    try {
        const response = await fetch('http://localhost:8080/api/v1/user/me/extend-session', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (response.ok) {
            console.info("Session extended successfully");
        } else if (response.status === 401) {
            console.warn("Session expired, redirecting to login...");
            window.location.href = '/login.html';
        } else {
            const errorText = await response.text();
            console.warn(`Session extension failed: ${errorText}`);
        }
    } catch (err) {
        console.error("Session extension failed:", err.message);
    }
});