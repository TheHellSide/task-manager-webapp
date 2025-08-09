// USER-DATA VALIDATION
const userData = JSON.parse(localStorage.getItem('loggedUser'));
if (!userData || !userData.token || !userData.id) {
    window.location.href = 'login.html';
}

const API_URL = "/api/v1/task";
const token = userData.token;
const userId = userData.id;

function getPriorityClass(priority) {
    switch (priority) {
        case "DEFAULT": return "alert alert-secondary";
        case "HIGH": return "alert alert-danger";
        case "MEDIUM": return "alert alert-warning";
        case "LOW": return "alert alert-success";
        default: return "alert alert-secondary";
    }
}

async function loadTasks() {
    try {
        const res = await fetch(`${API_URL}?userId=${userId}`, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });


        if (!res.ok) throw new Error("Errore nel caricamento dei task");
        const tasks = await res.json();
        const tasksList = document.getElementById("tasksList");
        tasksList.innerHTML = "";

        if (!tasks || tasks.length === 0) {
            tasksList.innerHTML = `<div class="no-tasks-message"><p class="text-center">No tasks for you yet. Create one →</p></div>`;
            return;
        }

        tasks.forEach(task => {
            const card = document.createElement("div");
            card.className = `mb-3 ${getPriorityClass(task.priority)}`;
            card.innerHTML = `
                <h5 class="task-title"><b>${task.title}</b></h5>
                <p class="task-description">${task.description || ""}</p>
                <p>
                    <small class="data-content"><span class="data-title">Created:</span> ${task.creationDate || ""}</small><br/>
                    <small class="data-content"><span class="data-title">Due:</span> ${task.dueDate || ""}</small>
                </p>
                <div style="font-weight: bold; color: inherit;">${task.priority}</div>
                <div class="mt-3">
                    <button class="btn btn-sm btn-${task.completed ? "secondary" : "success"} me-2" onclick="completeTask(${task.id})">
                        ${task.completed ? "Undo" : "Complete"}
                    </button>
                    <button class="btn btn-sm btn-warning me-2" onclick="startEditTask(${task.id})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteTask(${task.id})">Delete</button>
                </div>
            `;
            tasksList.appendChild(card);
        });
    }
    catch (error) {
        alert(error.message);
    }
}

async function startEditTask(id) {
    try {
        const res = await fetch(`${API_URL}/${id}`, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!res.ok) throw new Error("Errore nel caricamento del task");

        const task = await res.json();
        document.getElementById("formTitle").textContent = "Edit Task";
        document.getElementById("submitBtn").textContent = "Save Changes";
        document.getElementById("cancelEditBtn").classList.remove("d-none");

        document.getElementById("taskId").value = task.id || "";
        document.getElementById("title").value = task.title || "";
        document.getElementById("description").value = task.description || "";
        document.getElementById("creationDate").value = task.creationDate || "";
        document.getElementById("dueDate").value = task.dueDate || "";
        document.getElementById("priority").value = task.priority || "LOW";
    }
    catch (error) {
        alert(error.message);
    }
}

document.getElementById("cancelEditBtn").addEventListener("click", e => {
    e.preventDefault();
    clearForm();
});

document.getElementById("taskForm").addEventListener("submit", async e => {
    e.preventDefault();
    const taskId = document.getElementById("taskId").value;
    const payload = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        dueDate: document.getElementById("dueDate").value,
        priority: document.getElementById("priority").value,
        user_id: userId
    };

    try {
        let method, endpoint;
        if (taskId) {
            method = "PUT";
            endpoint = `${API_URL}/${taskId}`;
        }
        else {
            method = "POST";
            endpoint = API_URL;
        }

        const res = await fetch(endpoint, {
            method,
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(payload)
        });

        if (!res.ok) throw new Error("Errore nel salvataggio del task");

        clearForm();
        loadTasks();
    }
    catch (error) {
        alert(error.message);
    }
});

async function deleteTask(id) {
    try {
        const res = await fetch(`${API_URL}/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });
        if (!res.ok) throw new Error("Errore eliminazione");

        loadTasks();
    }
    catch (error) {
        alert(error.message);
    }
}

async function completeTask(id) {
    try {
        const res = await fetch(`${API_URL}/${id}/check`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token
            }
        });
        if (!res.ok) throw new Error("Errore aggiornamento stato");

        loadTasks();
    }
    catch (error) {
        alert(error.message);
    }
}

function clearForm() {
    document.getElementById("formTitle").textContent = "Create New Task";
    document.getElementById("submitBtn").textContent = "Add Task";
    document.getElementById("cancelEditBtn").classList.add("d-none");
    ["taskId", "title", "description", "creationDate", "dueDate", "priority"].forEach(id => {
        document.getElementById(id).value = "";
    });
}

loadTasks();