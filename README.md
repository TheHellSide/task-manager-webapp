<div align="center">

<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=4338ca&height=140&section=header&text=Task%20Manager&fontSize=44&fontColor=ffffff&fontAlignY=58&animation=fadeIn"/>

<img src="screenshots/logo.png" alt="Task Manager" width="100" />

<p>A self-hosted task manager built with Spring Boot and vanilla JavaScript.<br/>No cloud. No build step on the frontend. Just works.</p>

<p>
  <a href="https://github.com/TheHellSide/task-manager-webapp/releases"><img src="https://img.shields.io/github/v/release/TheHellSide/task-manager-webapp?style=flat-square&color=4338ca&label=release" alt="Latest Release"/></a>
  <img src="https://img.shields.io/badge/java-24-f97316?style=flat-square&logo=openjdk&logoColor=white" alt="Java 24"/>
  <img src="https://img.shields.io/badge/spring%20boot-3.x-22c55e?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/mariadb-10.x-c0765a?style=flat-square&logo=mariadb&logoColor=white" alt="MariaDB"/>
  <img src="https://img.shields.io/badge/license-MIT-3b82f6?style=flat-square" alt="MIT"/>
</p>

</div>

---

## Screenshots

<div align="center">
  <table border="0" cellspacing="0" cellpadding="10">
    <tr>
      <td align="center" valign="middle">
        <img src="screenshots/login.png" alt="Login" width="210"/>
        <br/><sub><b>Login</b></sub>
      </td>
      <td align="center" valign="middle">
        <img src="screenshots/dashboard.png" alt="Task Dashboard" width="420"/>
        <br/><sub><b>Task Dashboard</b></sub>
      </td>
      <td align="center" valign="middle">
        <img src="screenshots/user.png" alt="Profile" width="210"/>
        <br/><sub><b>Profile</b></sub>
      </td>
    </tr>
  </table>
</div>

---

## Features

<div align="center">

| | |
|:---:|:---|
| ![](https://img.shields.io/badge/Tasks-4338ca?style=flat-square) | Create, edit, delete, and complete tasks — title, description, due date, priority (`LOW` / `MEDIUM` / `HIGH`) |
| ![](https://img.shields.io/badge/Expiry-ef4444?style=flat-square) | Tasks past their due date are automatically flagged as `EXPIRED` |
| ![](https://img.shields.io/badge/Users-22c55e?style=flat-square) | Register, log in, update profile, change password, delete account |
| ![](https://img.shields.io/badge/Auth-f97316?style=flat-square) | UUID v4 tokens, SHA‑256 hashed in DB, delivered as HttpOnly cookies |
| ![](https://img.shields.io/badge/XSS-eab308?style=flat-square) | All task output HTML‑escaped server‑side on every response |
| ![](https://img.shields.io/badge/Theme-8b5cf6?style=flat-square) | Dark / light mode persisted in `localStorage`, no flicker on reload |

</div>

---

## Stack

<div align="center">

![Java](https://img.shields.io/badge/Java%2024-f97316?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-22c55e?style=for-the-badge&logo=springboot&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-c0765a?style=for-the-badge&logo=mariadb&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-e34f26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572b6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript%20ES6+-f7df1e?style=for-the-badge&logo=javascript&logoColor=black)
![Bootstrap](https://img.shields.io/badge/Bootstrap%205.3-7952b3?style=for-the-badge&logo=bootstrap&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-c71a36?style=for-the-badge&logo=apachemaven&logoColor=white)

</div>

---

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

---

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

---

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

<div align="center">
<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=4338ca&height=80&section=footer"/>
</div>
