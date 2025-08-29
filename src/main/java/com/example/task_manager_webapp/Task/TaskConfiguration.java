package com.example.task_manager_webapp.Task;

import com.example.task_manager_webapp.User.User;
import com.example.task_manager_webapp.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.time.LocalDate;
import java.util.List;

// @Configuration
public class TaskConfiguration {
    @Bean
    @Order(2)
    CommandLineRunner taskCommandLineRunner(TaskRepository taskRepository, UserRepository userRepository) {
        return args -> {
            User admin = userRepository.findUserByEmail("admin@admin.com")
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            Task DEFAULT_TASK = new Task(
                    "New task",
                    "Task description. This is an example of a simple task.",
                    LocalDate.now(),
                    TaskPriority.DEFAULT,
                    admin
            );

            for (Task task : List.of(DEFAULT_TASK)) {
                    // DEFAULT TASK
                    taskRepository.save(task);
            }
        };
    }
}
