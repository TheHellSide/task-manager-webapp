package com.example.to_do_list.Task;

import com.example.to_do_list.User.User;
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
    public ResponseEntity<String> addNewTask(@RequestBody TaskRequestDTO taskDTO) {
        boolean added = taskService.addNewTask(taskDTO);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully added.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or error while saving task.");
    }

    @DeleteMapping(path = "{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully deleted.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
    }

    @GetMapping(path = "{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "{taskId}")
    public ResponseEntity<String> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskDTO
    ) {
        boolean updated = taskService.updateTask(taskId,
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getDueDate(),  // passa LocalDate direttamente
                taskDTO.getPriority()
        );
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