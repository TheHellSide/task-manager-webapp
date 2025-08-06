package com.example.to_do_list.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public boolean addNewTask(Task task) {
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

    public boolean updateTask(
            Long taskId,
            String title,
            String description,
            String dueDate,
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

        if (dueDate != null && !dueDate.isBlank()) { //TODO: VERIFY THE DATA.
            task.setDueDate(LocalDate.parse(dueDate));
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