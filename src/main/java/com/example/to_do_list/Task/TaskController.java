package com.example.to_do_list.Task;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(("/api/v1/task"))
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> tasks(){
        return taskService.getTasks();
    }

//    @PostMapping
//    public void addNewTask(Task task){
//        taskService.addNewTask(task);
//    }
//
//    @DeleteMapping(path = "{taskId}")
//    public void deleteTask(@PathVariable Long taskId) {
//        taskService.deleteTask(taskId);
//    }
//
//    @PutMapping(path = "{taskId}")
//    public void updateTask(
//            @PathVariable Long taskId,
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) String dueDate,
//            @RequestParam(required = false) TaskPriority priority
//    ) {
//        taskService.updateTask(taskId, title, description, dueDate, priority);
//    }
//
//    @PutMapping(path = "{taskId}")
//    public void checkTask(
//            @PathVariable Long taskId,
//            @RequestParam(required = false) boolean completed
//    ) {
//        taskService.checkTask(taskId, completed);
//    }
}

