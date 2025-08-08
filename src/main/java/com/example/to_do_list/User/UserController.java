package com.example.to_do_list.User;

import com.example.to_do_list.User.Logs.LoginRequest;
import com.example.to_do_list.User.Logs.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(("/api/v1/user"))
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        boolean registered = userService.registerNewUser(user);
        if (registered) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
    }


    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token
    ) {
        boolean deleted = userService.deleteUser(removeBearerPrefix(token), userId);
        if (deleted) {
            return ResponseEntity.ok("User successfully deleted.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        boolean updated = userService.updateUser(removeBearerPrefix(token), userId, username, email);
        if (updated) {
            return ResponseEntity.ok("User information successfully updated.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PostMapping(path = "{userId}/verify-password")
    public ResponseEntity<String> verifyOldPassword(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordRequest passwordRequest) {

        boolean valid = userService.checkPassword(removeBearerPrefix(token), userId, passwordRequest.getPassword());
        if (valid) {
            return ResponseEntity.ok("Password verified.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
    }

    @PutMapping(path = "{userId}/password")
    public ResponseEntity<String> updateUserPassword(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordRequest passwordRequest) {

        boolean updated = userService.updateUserPassword(removeBearerPrefix(token), userId, passwordRequest.getPassword());
        if (updated) {
            return ResponseEntity.ok("Password successfully updated.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<LoginResponse> loginResponse = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (loginResponse.isPresent()) {
            return ResponseEntity.ok(loginResponse);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    public String removeBearerPrefix(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return token;
    }
}