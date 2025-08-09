package com.example.to_do_list.User;

import com.example.to_do_list.Security.Token;
import com.example.to_do_list.Security.TokenService;
import com.example.to_do_list.Task.TaskRepository;
import com.example.to_do_list.Task.TaskService;
import com.example.to_do_list.User.Logs.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService, TaskRepository  taskRepository, TaskService taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
        this.tokenService = tokenService;
        this.taskRepository  = taskRepository;
    }

    public Optional<LoginResponse> login(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null)
            return Optional.empty();

        Token userToken = tokenService.generateToken(user);
        return Optional.of(new LoginResponse(
                user.getId(),
                userToken.getToken(),
                user.getUsername(),
                user.getEmail()
        ));
    }

    public boolean registerNewUser(User user) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository
                .findByUsernameOrEmail(user.getUsername(), user.getEmail()));

        if (optionalUser.isEmpty()){
            userRepository.save(user);
            taskService.createDefaultTask(user);

            return true;
        }

        return false;
    }

    @Transactional
    public boolean deleteUser(String token, Long userId) {
        boolean exists = userRepository.existsById(userId);

        if (
                exists &&
                tokenService.isValidToken(token) &&
                tokenService.isTokenValidForUser(token, userId)
        ) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty())
                return false;

            User user = userOptional.get();

            // DELETE TASKS AND TOKENS
            taskRepository.deleteAllByUserId(userId);
            tokenService.deleteUserTokens(user);

            // DELETE USER
            userRepository.delete(user);

            return true;
        }

        return false;
    }

    public boolean updateUser(
            String token,
            Long userId,
            String username,
            String email
    ) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return false;
        }
        else {
            if (
                    !tokenService.isValidToken(token) ||
                    !tokenService.isTokenValidForUser(token, userId)
            ) {
                return false;
            }
        }

        User user = optionalUser.get();

        if (username != null && !username.isBlank() && !username.equals(user.getUsername())) {
            Optional<User> userOptional = userRepository
                    .findUserByUsername(username);

            if (userOptional.isEmpty())
                user.setUsername(username);
        }

        if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
            Optional<User> userOptional = userRepository
                    .findUserByEmail(email);

            if (userOptional.isEmpty())
                user.setEmail(email);
        }

        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(String token, Long userId, String oldPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }
        else {
            if (
                    !tokenService.isValidToken(token) ||
                    !tokenService.isTokenValidForUser(token, userId)
            ) {
                return false;
            }
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        return true;
    }

    public boolean updateUserPassword(String token, Long userId, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }
        else {
            if (
                    !tokenService.isValidToken(token) ||
                            !tokenService.isTokenValidForUser(token, userId)
            ) {
                return false;
            }
        }

        User user = optionalUser.get();

        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }
}