package com.example.to_do_list.Task;

import com.example.to_do_list.Security.TokenService;
import com.example.to_do_list.User.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(("/api/v1/task"))
public class TaskController {
    private final TaskService taskService;
    private final UserController userController;

    public TaskController(TaskService taskService, UserController userController) {
        this.taskService = taskService;
        this.userController = userController;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks(
            @RequestHeader("Authorization") String token,
            @RequestParam Long userId
    ) {
        Optional<List<Task>> userTaskOptional = taskService.
                getUserTasks(userController.removeBearerPrefix(token), userId);

        if (userTaskOptional.isPresent()) {
            return ResponseEntity.ok(userTaskOptional.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> addNewTask(
            @RequestHeader("Authorization") String token,
            @RequestBody TaskRequestDTO taskDTO
    ) {
        boolean added = taskService.addNewTask(taskDTO);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully added.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or error while saving task.");
    }

    @DeleteMapping(path = "{taskId}")
    public ResponseEntity<String> deleteTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long taskId
    ) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully deleted.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
    }

    @GetMapping(path = "{taskId}")
    public ResponseEntity<Task> getTaskById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long taskId
    ) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping(path = "{taskId}")
    public ResponseEntity<String> updateTask(
            @RequestHeader("Authorization") String token,
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO taskDTO
    ) {
        boolean updated = taskService.updateTask(taskId,
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getDueDate(),
                taskDTO.getPriority()
        );
        if (updated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Task information successfully updated.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
    }

    @PutMapping(path = "{taskId}/check")
    public ResponseEntity<String> checkTask(
            @RequestHeader("Authorization") String token,
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