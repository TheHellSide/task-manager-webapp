package com.example.to_do_list.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    //TODO
    public boolean addNewTask(Task task) {
        return true;
    }

    public boolean deleteTask(Long taskId) {
        return true;
    }

    public boolean updateTask(
            Long taskId,
            String title,
            String description,
            String dueDate,
            TaskPriority priority
    ) {
        return true;
    }

    public boolean checkTask(Long taskId, boolean completed) {
        return true;
    }
}
