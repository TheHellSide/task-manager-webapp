# Task Manager App (Spring Boot)

A simple and powerful Task Manager web application built with **Java** and **Spring Boot**.
This project is meant to help you manage your tasks and users efficiently while also serving as a cool portfolio project!

### 🧠 Features

- Create, Read, Update, Delete (CRUD) tasks with APIs
- Task prioritization (via `TaskPriority`)
- User registration & login
- User management with services and repositories
- Simple front-end using HTML/CSS and JavaScript (no frameworks)
- Theme toggle with JavaScript
- Data persistence with Spring Data JPA & SQL
- Pre-configured SQL schema included

### 📁 Tech Stack

- Java 17+ (Java 24)
- Spring Boot
- Spring Data JPA
- MySQL (configurable)
- HTML/CSS/JS
- Maven

### 💾 Project Structure

```bash
.
├── LICENSE
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── task_manager_webapp/
│   │   │               ├── Security/
│   │   │               │   ├── ...
│   │   │               │   └── Token/
│   │   │               │       └── ...
│   │   │               ├── Task/
│   │   │               │   └── ...
│   │   │               ├── User/
│   │   │               │   ├── Logs/
│   │   │               │   │   └── ...
│   │   │               │   └── ...
│   │   │               └── TaskManagerApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── ...
│   │       │   ├── js/
│   │       │   │   ├── global/
│   │       │   │   │   └── ...
│   │       │   │   └── ...
│   │       │   ├── index.html
│   │       │   ├── login.html
│   │       │   ├── register.html
│   │       │   ├── dashboard.html
│   │       │   └── user.html
│   │       ├── application.properties
│   │       └── dump.sql
│   └── test
│       └── ...
└── target
    └── ...
```

### 📦 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/TheHellSide/task-manager-webapp.git
   ```

2. Navigate to the project directory:
   ```bash
   cd task-manager-webapp
   ```

3. Use the **_dump.sql_** file to create the database, only then Run with Maven:
   ```bash
   mysql -u root -p task_manager_webapp < dump.sql
   ./mvnw spring-boot:run
   ```
   
> **REMEMBER**:
> Change the credentials of the database in **application.properties**. Those are some '**STANDARD**' and **unsecure** credentials.

4. Open your browser and go to:
   ```
   http://localhost:8080
   ```

### 📦 How to Use It?

**If you don't know how to use it, why are you here? ;)**
But seriously:
Just register as a new user, log in, and start adding your tasks.
Toggle between light/dark themes and manage your daily life like a pro.

### 📄 License

This project is licensed under the [MIT License](LICENSE).

### 👥 Author

Made with by **TheHellSide**
Follow me on [GitHub](https://github.com/TheHellSide)

### ⭐ Support

If you like this project, leave a star — it helps a lot!
