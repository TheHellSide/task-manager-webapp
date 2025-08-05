package com.example.to_do_list.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(("/api/v1/user"))
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> users(){
        return userService.getUsers();
    }

    // TODO: POST, DELETE, PUT in Service
    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        boolean registered = userService.registerNewUser(user);
        if (registered) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
    }


    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.ok("User successfully deleted.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        boolean updated = userService.updateUser(userId, username, email);
        if (updated) {
            return ResponseEntity.ok("User information successfully updated.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PutMapping(path = "{userId}/password")
    public ResponseEntity<String> updateUserPassword(
            @PathVariable Long userId,
            @RequestParam(required = false) String password
    ) {
        boolean updated = userService.updateUserPassword(userId, password);
        if (updated) {
            return ResponseEntity.ok("Password successfully updated.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
