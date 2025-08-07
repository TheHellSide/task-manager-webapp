# To-Do List App (Spring Boot)

A simple and powerful To-Do List web application built with **Java**, **Spring Boot**, and **Thymeleaf**.
This project is meant to help you manage your tasks and users efficiently while also serving as a cool portfolio project!

## Features

- Create, Read, Update, Delete (CRUD) tasks with APIs
- Task prioritization (via `TaskPriority`)
- User registration & login
- User management with services and repositories
- Simple front-end using HTML/CSS and JavaScript (no frameworks)
- Theme toggle with JavaScript
- Data persistence with Spring Data JPA & SQL
- Pre-configured SQL schema included

## Tech Stack

- Java 17+ (Java 24)
- Spring Boot
- Spring Data JPA
- Thymeleaf
- MySQL (configurable)
- HTML/CSS/JS
- Maven

## Project Structure

```bash
src
├── main
│   ├── java
│   │   └── com.example.to_do_list
│   │       ├── Task                # All task-related logic
│   │       └── User                # All user-related logic
│   ├── resources
│   │   ├── static                  # Front-end HTML + JS
│   │   ├── templates               # Thymeleaf (optional)
│   │   └── application.properties  # App configuration
└── test
    └── ...                         # Unit and integration tests
```

## How to Run
Wait, if you do not know how to run it, why are you here? ;)

1. Clone this repository:
   ```bash
   git clone https://github.com/TheHellSide/to-do-list-springboot.git
   ```

2. Navigate to the project directory:
   ```bash
   cd to-do-list-springboot
   ```

3. Run with Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Open your browser and go to:
   ```
   http://localhost:8080
   ```

## How to Use It?

**If you don't know how to use it, why are you here? 😉**

But seriously:
Just register as a new user, log in, and start adding your tasks.
Toggle between light/dark themes and manage your daily life like a pro.

## License

This project is licensed under the [MIT License](LICENSE).

## Author

Made with by **TheHellSide**
Follow me on [GitHub](https://github.com/TheHellSide)

## Support

If you like this project, leave a star — it helps a lot!
