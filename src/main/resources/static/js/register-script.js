const API_URL = 'http://localhost:8080/api/v1/user';
const errorDiv = document.getElementById('errorMsg');

// REGISTRATION
document.getElementById('registerForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    errorDiv.style.display = 'none';
    errorDiv.textContent = '';

    const email = document.getElementById('email').value.trim();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    const userData = { email, username, password };

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (response.status === 201) {
            // REGISTRATION REDIRECT
            window.location.href = 'login.html';
        }
        else if (response.status === 400) {
            const errorMessage = await response.text();
            errorDiv.textContent = "Registration failed: " + errorMessage;
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