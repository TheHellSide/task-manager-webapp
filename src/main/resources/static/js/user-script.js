document.addEventListener("DOMContentLoaded", () => {
    // USER-DATA VALIDATION
    const loggedUser = JSON.parse(localStorage.getItem('loggedUser'));
    if (!loggedUser) {
        window.location.href = 'login.html';
        return;
    }

    document.getElementById('username').value = loggedUser.username;
    document.getElementById('email').value = loggedUser.email;

    const API_BASE = 'http://localhost:8080/api/v1/user';

    // UPDATE USER-INFORMATIONS
    document.getElementById('updateForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        clearMessages();

        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();

        try {
            const res = await fetch(`${API_BASE}/me`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ username, email })
            });

            if (res.ok) {
                showSuccess('User information updated successfully.');
                loggedUser.username = username;
                loggedUser.email = email;
                localStorage.setItem('loggedUser', JSON.stringify(loggedUser));
            } else {
                const msg = await res.text();
                showError(msg);
            }
        }
        catch (err) {
            showError('Network error: ' + err.message);
        }
    });

    // PASSWORD CHANGE
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
            const verifyRes = await fetch(`${API_BASE}/me/verify-password`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ password: current })
            });

            if (!verifyRes.ok) {
                showError('Current password is incorrect.');
                return;
            }

            const updateRes = await fetch(`${API_BASE}/me/password`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ password: newPass })
            });

            if (updateRes.ok) {
                showSuccess('Password changed successfully.');
                e.target.reset();
            } else {
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
        localStorage.removeItem('loggedUser');

        try {
            const res = await fetch(`${API_BASE}/me`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (res.ok) {
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