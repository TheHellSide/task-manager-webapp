package com.example.to_do_list.Task;

import com.example.to_do_list.Security.Token;
import com.example.to_do_list.Security.TokenRepository;
import com.example.to_do_list.Security.TokenService;
import com.example.to_do_list.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TokenRepository tokenRepository, TokenService tokenService) {
        this.taskRepository = taskRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
    }

    public Optional<List<Task>> getUserTasks(String token){
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);

        if (
                tokenOptional.isEmpty() ||
                !tokenService.isValidToken(token)
        ) {
            return Optional.empty();
        }

        User user = tokenOptional.get().getUser();
        return Optional.of(taskRepository.findAllByUser(user));
    }

    public boolean addNewTask(
            String token,
            TaskRequestDTO taskDto
    ) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            return false;
        }

        Task task = new Task(
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getDueDate(),
                taskDto.getPriority(),
                tokenOptional.get().getUser()
        );

        taskRepository.save(task);
        return true;
    }

    public boolean deleteTask(
            String token,
            Long taskId
    ) {
        Optional<Task> optionalTask = getTaskByIdIfValidToken(token, taskId);

        if (optionalTask.isEmpty())
            return false;

        if (optionalTask.isPresent()) {
            taskRepository.delete(optionalTask.get());
            return true;
        }

        return false;
    }

    // NEVER_USED
    @Transient
    public boolean deleteAllTasks(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isEmpty() || !tokenService.isValidToken(token))
            return false;

        taskRepository.deleteAllByUserId(
                tokenOptional.get().getUser().getId()
        );

        return true;
    }

    public boolean updateTask(
            String token,
            Long taskId,
            String title,
            String description,
            LocalDate dueDate,
            TaskPriority priority
    ) {
        Optional<Task> optionalTask = getTaskByIdIfValidToken(token, taskId);

        if (optionalTask.isEmpty())
            return false;

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

    public Optional<Boolean> checkTask(
            String token,
            Long taskId
    ) {
        Optional<Task> optionalTask = getTaskByIdIfValidToken(token, taskId);

        if (optionalTask.isEmpty())
            return Optional.empty();

        Task task = optionalTask.get();
        task.setCompleted(!task.isCompleted());

        taskRepository.save(task);
        return Optional.of(task.isCompleted());
    }

    public void createDefaultTask(User user) {
        Task DEFAULT_TASK = new Task(
                "New task",
                "Task description. This is an example of a simple task.",
                LocalDate.now(),
                TaskPriority.DEFAULT,
                user
        );

        taskRepository.save(DEFAULT_TASK);
    }

    public Optional<Task> getTaskByIdIfValidToken(
            String token,
            Long taskId
    ) {
        Optional<Token> optionalToken = tokenRepository
                .findByToken(token);

        // TOKEN-VALIDATION
        if (optionalToken.isEmpty() || !tokenService.isValidToken(token)) {
            return Optional.empty();
        }

        Optional<Task> optionalTask = taskRepository.
                findById(taskId);

        // TASK AND USER-VALIDATION
        if (
                optionalTask.isEmpty() ||
                !optionalTask.get().getUser().equals(optionalToken.get().getUser())
        ) {
            return Optional.empty();
        }

        return optionalTask;
    }
}