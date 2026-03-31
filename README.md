<div align="center">

  <img src="screenshots/logo.png" alt="Task Manager Logo" width="120" />

  <h1>Task Manager</h1>

  <p>A self-hosted task manager built with Spring Boot and vanilla JavaScript.<br/>No frontend frameworks. No cloud dependencies. Just works.</p>

  <p>
    <a href="https://github.com/TheHellSide/task-manager-webapp/releases"><img src="https://img.shields.io/github/v/release/TheHellSide/task-manager-webapp?color=4f46e5&label=release" alt="Latest Release"/></a>
    <img src="https://img.shields.io/badge/java-24-f89820?logo=openjdk&logoColor=white" alt="Java 24"/>
    <img src="https://img.shields.io/badge/spring%20boot-3.x-6db33f?logo=springboot&logoColor=white" alt="Spring Boot"/>
    <img src="https://img.shields.io/badge/mariadb-10.x-c0765a?logo=mariadb&logoColor=white" alt="MariaDB"/>
    <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue" alt="MIT License"/></a>
  </p>

</div>

---

## Screenshots

<div align="center">
  <table>
    <tr>
      <td align="center"><strong>Login</strong></td>
      <td align="center"><strong>Dashboard</strong></td>
      <td align="center"><strong>Profile</strong></td>
    </tr>
    <tr>
      <td><img src="screenshots/login.png" alt="Login page" width="280"/></td>
      <td><img src="screenshots/dashboard.png" alt="Task dashboard" width="280"/></td>
      <td><img src="screenshots/user.png" alt="User profile" width="280"/></td>
    </tr>
  </table>
</div>

---

## Features

- **Task management** — create, edit, delete, and complete tasks with title, description, due date, and priority (`LOW` / `MEDIUM` / `HIGH`)
- **Auto-expiry** — tasks past their due date are automatically flagged as `EXPIRED`
- **User accounts** — register, log in, update your profile, change password, or delete your account
- **Token auth** — session tokens are UUID v4 values, SHA-256 hashed before DB storage, delivered via HttpOnly cookies
- **XSS protection** — all task output is HTML-escaped server-side on every response
- **Dark / light theme** — persisted in `localStorage`, no flicker on reload
- **No build step for the frontend** — plain HTML, CSS, and JavaScript; just serve and go

## Stack

| | |
|---|---|
| Backend | Java 24, Spring Boot 3, Spring Data JPA |
| Database | MariaDB |
| Security | BCrypt (passwords), SHA-256 (tokens) |
| Frontend | HTML5, CSS3, ES6+, Bootstrap 5.3 |
| Build | Maven (wrapper included) |

## Getting started

**Prerequisites:** Java 17+, Maven (or use `./mvnw`), MariaDB running locally.

```bash
# 1. Clone
git clone https://github.com/TheHellSide/task-manager-webapp.git
cd task-manager-webapp

# 2. Create the database
mysql -u root -p < src/main/resources/dump.sql

# 3. Set your DB credentials in src/main/resources/application.properties
#    spring.datasource.username and spring.datasource.password

# 4. Run
./mvnw spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080), register an account, and start adding tasks.

> **Before deploying:** change the default DB credentials, set `cookie.secure=true`, and disable `show-sql` and `DEBUG` logging in `application.properties`.

## API reference

All endpoints live under `/api/v1`. Auth is handled by the `authentication-token` HttpOnly cookie set at login.

<details>
<summary><strong>Users</strong> — <code>/api/v1/user</code></summary>

| Method | Path | Description |
|---|---|---|
| `POST` | `/` | Register |
| `POST` | `/in` | Login |
| `POST` | `/out` | Logout |
| `PUT` | `/me` | Update username & email |
| `DELETE` | `/me` | Delete account |
| `POST` | `/me/verify-password` | Verify current password |
| `PUT` | `/me/password` | Change password |

</details>

<details>
<summary><strong>Tasks</strong> — <code>/api/v1/task</code></summary>

| Method | Path | Description |
|---|---|---|
| `GET` | `/` | List all tasks |
| `POST` | `/` | Create task |
| `GET` | `/{id}` | Get task |
| `PUT` | `/{id}` | Update task |
| `DELETE` | `/{id}` | Delete task |
| `PUT` | `/{id}/check` | Toggle completion |

</details>

## Project structure

```
src/main/
├── java/com/example/task_manager_webapp/
│   ├── security/          # BCrypt helper, SHA-256 token hashing
│   │   └── tokens/        # Token entity, service, repository, controller
│   ├── users/             # User entity, service, controller, repository, DTOs
│   └── tasks/             # Task entity, service, controller, repository, DTOs
└── resources/
    ├── application.properties
    ├── dump.sql
    └── static/            # login, register, dashboard, user pages + CSS/JS
```

## License

[MIT](LICENSE) — do whatever you want, attribution appreciated.

## Author

**TheHellSide** · [github.com/TheHellSide](https://github.com/TheHellSide)
