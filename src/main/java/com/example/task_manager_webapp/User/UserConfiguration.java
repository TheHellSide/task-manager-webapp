package com.example.task_manager_webapp.User;

import com.example.task_manager_webapp.Task.Task;
import com.example.task_manager_webapp.Task.TaskPriority;
import com.example.task_manager_webapp.Task.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Configuration
public class UserConfiguration {
    @Bean
    @Order(1)
    CommandLineRunner userCommandLineRunner (UserRepository userRepository, TaskService taskService){
        // The codebase is structured to support multiple users.
        return args -> {
            User admin = new User(
                    "admin@admin.com",
                    "admin",
                    "admin123"
            );

            for (User user : List.of(admin)){
                if (!userExists(userRepository, user.getUsername(), user.getEmail())){
                    userRepository.save(user);

                    Task DEFAULT_TASK = new Task(
                            "New task",
                            "Task description. This is an example of a simple task.",
                            LocalDate.now(),
                            TaskPriority.DEFAULT,
                            user
                    );

                    taskService.createDefaultTask(user);
                }
            }
        };
    }

    private boolean userExists(UserRepository userRepository, String username, String email){
        Optional<User> userOptional = userRepository
                .findUserByUsernameOrEmail(username, email);

        return userOptional.isPresent();
    }
}