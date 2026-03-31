const API_URL = 'http://localhost:8080/api/v1/user';
const errorDiv = document.getElementById('errorMsg');

// REGISTRATION
document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    errorDiv.style.display = 'none';
    errorDiv.textContent = '';

    const rawEmail = document.getElementById('email').value;
    const rawUsername = document.getElementById('username').value;
    const rawPassword = document.getElementById('password').value;
    const confirm_password = document.getElementById('confirm-password').value;

    const clean = sanitizeForm(
        { email: 'email', username: 'username', password: 'password' },
        { email: rawEmail, username: rawUsername, password: rawPassword }
    );

    if (warnIfInvalidChars(rawEmail, clean.email, 'Email'))
        return;
    if (warnIfInvalidChars(rawUsername, clean.username, 'Username'))
        return;

    if (clean.password !== confirm_password) {
        errorDiv.textContent = 'Passwords do not match.';
        errorDiv.style.display = 'block';
        return;
    }

    const userData = { email: clean.email, username: clean.username, password: clean.password };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        if (response.status === 201) {
            window.location.href = 'login.html';
        }
        else if (response.status === 400) {
            const errorMessage = await response.text();
            errorDiv.textContent = 'Registration failed: ' + errorMessage;
            errorDiv.style.display = 'block';
        }
        else {
            errorDiv.textContent = 'Unexpected error: ' + response.statusText;
            errorDiv.style.display = 'block';
        }
    }
    catch (error) {
        errorDiv.textContent = 'Network error: ' + error.message;
        errorDiv.style.display = 'block';
    }
});