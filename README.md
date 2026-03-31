# Task Manager Web App

> A full-stack task management application built with **Java 24 + Spring Boot** on the backend and plain **HTML / CSS / JavaScript** on the frontend.

[![Java](https://img.shields.io/badge/Java-24-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![MariaDB](https://img.shields.io/badge/MariaDB-10.x-blue?logo=mariadb)](https://mariadb.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📸 Screenshots

> 📌 Place your screenshots in the `screenshots/` folder as `login.png`, `dashboard.png`, and `user.png`.

### Login Page
![Login Page](screenshots/login.png)

### Task Dashboard
![Task Dashboard](screenshots/dashboard.png)

### User Profile
![User Profile](screenshots/user.png)

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 Authentication | Custom token-based auth with HttpOnly cookies; tokens are SHA-256 hashed before DB storage |
| 👤 User Management | Register, login, update username / email, change password, delete account |
| ✅ Task CRUD | Create, read, update, delete tasks via a clean REST API |
| 🎯 Task Priorities | `LOW`, `MEDIUM`, `HIGH`, `EXPIRED`, `DEFAULT` — expired tasks are flagged automatically |
| 🌓 Theme Toggle | Light / dark mode persisted across sessions via `localStorage` |
| 🛡️ Input Sanitization | Server-side HTML escaping (Apache Commons Text) + client-side character filtering |
| 💾 Data Persistence | Spring Data JPA with MariaDB; pre-built `dump.sql` schema included |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 24 |
| Framework | Spring Boot 3 |
| ORM | Spring Data JPA / Hibernate |
| Database | MariaDB |
| Security | BCrypt (passwords) + SHA-256 (tokens) |
| Frontend | HTML5, CSS3, JavaScript (ES6+), Bootstrap 5.3 |
| Build | Apache Maven (Maven Wrapper included) |

---

## 📁 Project Structure

```
task-manager-webapp/
├── pom.xml
├── dump.sql                              ← database schema
└── src/
    ├── main/
    │   ├── java/com/example/task_manager_webapp/
    │   │   ├── TaskManagerApplication.java
    │   │   ├── security/
    │   │   │   ├── Security.java         ← BCrypt + HTML escaping utilities
    │   │   │   └── tokens/
    │   │   │       ├── Token.java
    │   │   │       ├── TokenService.java
    │   │   │       ├── TokenRepository.java
    │   │   │       └── TokenController.java
    │   │   ├── users/
    │   │   │   ├── User.java
    │   │   │   ├── UserService.java
    │   │   │   ├── UserController.java
    │   │   │   ├── UserRepository.java
    │   │   │   ├── UserConfiguration.java
    │   │   │   └── dto/
    │   │   │       ├── PasswordRequest.java
    │   │   │       └── login/
    │   │   │           ├── LoginRequest.java
    │   │   │           └── LoginResponse.java
    │   │   └── tasks/
    │   │       ├── Task.java
    │   │       ├── TaskService.java
    │   │       ├── TaskController.java
    │   │       ├── TaskRepository.java
    │   │       ├── TaskPriority.java
    │   │       ├── TaskConfiguration.java
    │   │       └── dto/
    │   │           └── TaskRequestDTO.java
    │   └── resources/
    │       ├── application.properties
    │       ├── dump.sql
    │       └── static/
    │           ├── index.html
    │           ├── login.html
    │           ├── register.html
    │           ├── dashboard.html
    │           ├── user.html
    │           ├── css/
    │           │   ├── index-style.css
    │           │   ├── authentication-style.css
    │           │   ├── dashboard-style.css
    │           │   └── user-style.css
    │           └── js/
    │               ├── login-script.js
    │               ├── register-script.js
    │               ├── dashboard-script.js
    │               ├── user-script.js
    │               └── global/
    │                   ├── logout.js
    │                   ├── theme-toggle.js
    │                   ├── input-sanitizer.js
    │                   └── invalid-char-alert.js
    └── test/
        └── java/.../TaskManagerApplicationTests.java
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+** (tested on Java 24)
- **Maven** (or use the included `mvnw` wrapper)
- **MariaDB** server running locally

### 1. Clone the repository

```bash
git clone https://github.com/TheHellSide/task-manager-webapp.git
cd task-manager-webapp
```

### 2. Set up the database

```bash
mysql -u root -p < src/main/resources/dump.sql
```

### 3. Configure credentials

Open `src/main/resources/application.properties` and update the database credentials:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/task_manager_webapp
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
```

> ⚠️ **Important:** The default credentials (`root` / `admin123`) are for local development only. **Never deploy with these defaults.**

### 4. Build and run

```bash
./mvnw spring-boot:run
```

### 5. Open the app

Navigate to [http://localhost:8080](http://localhost:8080) in your browser.

---

## 📡 REST API Overview

All endpoints are prefixed with `/api/v1`. Authentication is handled via an HttpOnly cookie (`authentication-token`) set at login.

### Users — `/api/v1/user`

| Method | Path | Description |
|---|---|---|
| `POST` | `/` | Register a new user |
| `POST` | `/in` | Login — sets HttpOnly auth cookie |
| `POST` | `/out` | Logout — clears auth cookie |
| `PUT` | `/me` | Update username & email |
| `DELETE` | `/me` | Delete the authenticated user |
| `POST` | `/me/verify-password` | Verify current password before sensitive changes |
| `PUT` | `/me/password` | Change password |

### Tasks — `/api/v1/task`

| Method | Path | Description |
|---|---|---|
| `GET` | `/` | Get all tasks for the authenticated user |
| `POST` | `/` | Create a new task |
| `GET` | `/{id}` | Get a specific task by ID |
| `PUT` | `/{id}` | Update a task |
| `DELETE` | `/{id}` | Delete a task |
| `PUT` | `/{id}/check` | Toggle task completion status |

---

## 🔒 Security Notes

- Passwords are hashed with **BCrypt** before storage.
- Session tokens are **UUID v4** values hashed with **SHA-256** before being stored in the database; only the raw token is sent to the client via an HttpOnly cookie.
- Task titles and descriptions are **HTML-escaped** (server-side) on every response to prevent XSS.
- Client-side input sanitization provides an additional UX-level barrier.
- Set the `secure` flag to `true` on the auth cookie when deploying over HTTPS.
- Disable `show-sql=true` and `DEBUG` log levels before going to production.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 👤 Author

Crafted by **TheHellSide** — follow on [GitHub](https://github.com/TheHellSide)

---

## ⭐ Support

If you find this project useful, consider leaving a ⭐ — it really helps!
