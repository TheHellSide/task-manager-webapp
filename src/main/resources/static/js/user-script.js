document.addEventListener("DOMContentLoaded", () => {
    // USER-DATA VALIDATION
    const loggedUserStr = localStorage.getItem('loggedUser');
    if (!loggedUserStr) {
        window.location.href = 'login.html';
        return;
    }

    const loggedUser = JSON.parse(loggedUserStr);
    document.getElementById('username').value = loggedUser.username;
    document.getElementById('email').value = loggedUser.email;

    // Helper function to get Authorization header
    const getAuthHeaders = () => ({
        'Authorization': 'Bearer ' + loggedUser.token,
        'Content-Type': 'application/json'
    });

    // UPDATE USER-INFORMATIONS
    document.getElementById('updateForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        clearMessages();

        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();

        try {
            const res = await fetch(`http://localhost:8080/api/v1/user/${loggedUser.id}?username=${encodeURIComponent(username)}&email=${encodeURIComponent(email)}`, {
                method: 'PUT',
                headers: { 'Authorization': 'Bearer ' + loggedUser.token }
            });

            if (res.ok) {
                showSuccess('User information updated successfully.');
                loggedUser.username = username;
                loggedUser.email = email;
                localStorage.setItem('loggedUser', JSON.stringify(loggedUser));
            }
            else {
                const msg = await res.text();
                showError(msg);
            }
        }
        catch (err) {
            showError('Network error: ' + err.message);
        }
    });

    // PASSWORD
    document.getElementById('passwordForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        clearMessages();

        const current = document.getElementById('currentPassword').value;
        const newPass = document.getElementById('newPassword').value;
        const confirmPass = document.getElementById('confirmPassword').value;

        if (newPass !== confirmPass) {
            showError('New passwords do not match.');
            return;
        }

        try {
            const verifyRes = await fetch(`http://localhost:8080/api/v1/user/${loggedUser.id}/verify-password`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify({ password: current })
            });

            if (!verifyRes.ok) {
                showError('Current password is incorrect.');
                return;
            }

            const updateRes = await fetch(`http://localhost:8080/api/v1/user/${loggedUser.id}/password`, {
                method: 'PUT',
                headers: getAuthHeaders(),
                body: JSON.stringify({ password: newPass })
            });

            if (updateRes.ok) {
                showSuccess('Password changed successfully.');
                e.target.reset();
            }
            else {
                const msg = await updateRes.text();
                showError(msg);
            }
        }
        catch (err) {
            showError('Network error: ' + err.message);
        }
    });

    // DELETE ACCOUNT
    document.getElementById('deleteBtn').addEventListener('click', async () => {
        clearMessages();

        // REMOVE LOCAL DATA
        localStorage.removeItem('loggedUser');

        try {
            const res = await fetch(`http://localhost:8080/api/v1/user/${loggedUser.id}`, {
                method: 'DELETE',
                headers: { 'Authorization': 'Bearer ' + loggedUser.token }
            });

            if (res.ok) {
                // REDIRECT
                window.location.href = 'register.html';
            } else {
                const msg = await res.text();
                showError(msg);
            }
        } catch (err) {
            showError('Network error: ' + err.message);
        }
    });
});

function showError(message) {
    const msg = document.getElementById('message');
    msg.className = 'alert alert-danger';
    msg.textContent = message;
    msg.style.display = 'block';
}

function showSuccess(message) {
    const msg = document.getElementById('message');
    msg.className = 'alert alert-success';
    msg.textContent = message;
    msg.style.display = 'block';
}

function clearMessages() {
    const msg = document.getElementById('message');
    msg.style.display = 'none';
    msg.textContent = '';
}