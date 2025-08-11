package com.example.to_do_list.User;

import com.example.to_do_list.Security.Token.TokenService;
import com.example.to_do_list.User.Logs.LoginRequest;
import com.example.to_do_list.User.Logs.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(("/api/v1/user"))
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        boolean registered = userService.registerNewUser(user);
        if (registered) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists.");
    }

    @PostMapping("/in")
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        Optional<Map<String, Object>> dataOptional = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (dataOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        Map<String, Object> data = dataOptional.get();

        String token = (String) data.get("token");
        ResponseCookie cookie = ResponseCookie.from("authentication-token", token)
                .httpOnly(true)
                .secure(false) // HTTPS = true (localhost = false)
                .sameSite("Lax")
                .maxAge(24 * 60 * 60) // 24H
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok((LoginResponse) data.get("user"));
    }

    @PostMapping("/out")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = getTokenFromCookie(request);
        System.out.print(token);
        if (token != null) {
            userService.logout(response, token);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/extend-session")
    public  ResponseEntity<?> extendUserSession(
            @CookieValue(name = "authentication-token") String token,
            HttpServletResponse response
    ) {
        boolean isExtendedSession = tokenService.extendSession(token, response, 30);
        if (isExtendedSession) {
            return ResponseEntity.ok("Session extended.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not valid cookie.");
    }

    @DeleteMapping(path = "/me")
    public ResponseEntity<String> deleteUser(
            @CookieValue(name = "authentication-token") String token
    ) {
        boolean deleted = userService.deleteUser(token);
        if (deleted) {
            return ResponseEntity.ok("User successfully deleted.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PutMapping(path = "/me")
    public ResponseEntity<String> updateUser(
            @CookieValue(name = "authentication-token") String token,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        boolean updated = userService.updateUser(token, username, email);
        if (updated) {
            return ResponseEntity.ok("User information successfully updated.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @PostMapping(path = "me/verify-password")
    public ResponseEntity<String> verifyOldPassword(
            @CookieValue(name = "authentication-token") String token,
            @RequestBody PasswordRequest passwordRequest
    ) {
        boolean valid = userService.checkPassword(token, passwordRequest.getPassword());
        if (valid) {
            return ResponseEntity.ok("Password verified.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
    }

    @PutMapping(path = "me/password")
    public ResponseEntity<String> updateUserPassword(
            @CookieValue(name = "authentication-token") String token,
            @RequestBody PasswordRequest passwordRequest
    ) {

        boolean updated = userService.updateUserPassword(token, passwordRequest.getPassword());
        if (updated) {
            return ResponseEntity.ok("Password successfully updated.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("authentication-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}