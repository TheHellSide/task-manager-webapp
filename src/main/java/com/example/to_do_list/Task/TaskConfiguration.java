package com.example.to_do_list.Task;

import com.example.to_do_list.User.User;
import com.example.to_do_list.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class TaskConfiguration {
    @Bean
    @Order(2)
    CommandLineRunner taskCommandLineRunner(TaskRepository taskRepository, UserRepository userRepository) {
        return args -> {
            User admin = userRepository.findUserByEmail("admin@admin.com")
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            Task new_Task = new Task(
                    "New task",
                    "Task description. This is an example of a simple task.",
                    LocalDate.now(),
                    TaskPriority.HIGH,
                    admin
            );

            for (Task task : List.of(new_Task)) {
                if (task.getId() == 0){
                    taskRepository.save(task);
                }
            }
        };
    }
}
