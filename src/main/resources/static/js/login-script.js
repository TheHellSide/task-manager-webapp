const API_URL = 'http://localhost:8080/api/v1/user/in';
const errorDiv = document.getElementById('errorMsg');

// LOGIN
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    errorDiv.style.display = 'none';
    errorDiv.textContent = '';

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    try {
        const res = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include', // COOKIE-HANDLING
            body: JSON.stringify({ username, password }),
        });

        if (res.ok) {
            // USER-DATA
            const userData = await res.json();
            localStorage.setItem('loggedUser', JSON.stringify(userData));

            // REDIRECT
            window.location.href = 'dashboard.html';
        }
        else if (res.status === 401) {
            const err = await res.text();
            errorDiv.textContent = 'Login failed: ' + err;
            errorDiv.style.display = 'block';
        }
        else {
            errorDiv.textContent = 'Unexpected error: ' + res.statusText;
            errorDiv.style.display = 'block';
        }
    }
    catch (error) {
        errorDiv.textContent = 'Network error: ' + error.message;
        errorDiv.style.display = 'block';
    }
});
