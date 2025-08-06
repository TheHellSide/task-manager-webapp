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
            User anonymous = userRepository.findUserByEmail("anonymous@admin.com")
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            Task new_Task = new Task(
                    "New task",
                    "Task descriptio. This is an example of a simple task.",
                    LocalDate.now(),
                    TaskPriority.HIGH,
                    anonymous
            );


            for (Task task : List.of(new_Task)) {
                if (!taskRepository.findAll().contains(task)){
                    taskRepository.save(task);
                }
            }
        };
    }
}
