package com.example.to_do_list.Task;

import com.example.to_do_list.Security.TokenRepository;
import com.example.to_do_list.Security.TokenService;
import com.example.to_do_list.User.User;
import com.example.to_do_list.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TokenService tokenService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public Optional<List<Task>> getUserTasks(String token, Long userId){
        Optional<User> userOptional = userRepository.
                findById(userId);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        else {
            if (
                    !tokenService.isValidToken(token) ||
                    !tokenService.isTokenValidForUser(token, userId)
            ) {
                return Optional.empty();
            }
        }

        User user = userOptional.get();
        return Optional.of(taskRepository.findAllByUser(user));
    }

    public boolean addNewTask(TaskRequestDTO taskDto) {
        Optional<User> userOptional = userRepository.
                findById(taskDto.getUser_id());

        if (userOptional.isEmpty()) {
            return false;
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());
        task.setUser(userOptional.get());
        task.setCreationDate(LocalDate.now());
        task.setCompleted(false);

        taskRepository.save(task);
        return true;
    }

    public boolean deleteTask(Long taskId) {
        Optional<Task> optionalTask = taskRepository
                .findById(taskId);

        if (optionalTask.isPresent()) {
            taskRepository.delete(optionalTask.get());
            return true;
        }

        return false;
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public boolean updateTask(
            Long taskId,
            String title,
            String description,
            LocalDate dueDate,
            TaskPriority priority
    ) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            return false;
        }

        Task task = optionalTask.get();
        if (title != null && !title.isBlank()) {
            task.setTitle(title);
        }

        if (description != null && !description.isBlank()) {
            task.setDescription(description);
        }

        if (dueDate != null) {
            task.setDueDate(dueDate);
        }

        if (priority != null) {
            task.setPriority(priority);
        }

        taskRepository.save(task);
        return true;
    }

    public Optional<Boolean> checkTask(Long taskId) {
        Optional<Task> optionalTask = taskRepository
                .findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());

            taskRepository.save(task);
            return Optional.of(task.isCompleted());
        }

        return Optional.empty();
    }
}