package com.example.to_do_list.Task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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

    @PostMapping
    public ResponseEntity<String> addNewTask(@RequestBody Task task){
        boolean added = taskService.addNewTask(task);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully added to the workspace.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong while saving the task.");
    }

    @DeleteMapping(path = "{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully deleted.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
    }

    @PutMapping(path = "{taskId}")
    public ResponseEntity<String> updateTask(
            @PathVariable Long taskId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) TaskPriority priority
    ) {
        boolean updated = taskService.updateTask(taskId, title, description, dueDate, priority);
        if (updated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task information successfully updated.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
    }

    @PutMapping(path = "{taskId}/check")
    public ResponseEntity<String> checkTask(
            @PathVariable Long taskId
    ) {
        Optional<Boolean> checked = taskService
                .checkTask(taskId);

        if (checked.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(checked.get() ? "Task marked as completed." : "Task marked as not completed.");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Failed to update task status. Task may not exist.");
    }
}