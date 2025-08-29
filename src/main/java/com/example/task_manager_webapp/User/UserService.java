package com.example.task_manager_webapp.User;

import com.example.task_manager_webapp.Security.Token.Token;
import com.example.task_manager_webapp.Security.Token.TokenRepository;
import com.example.task_manager_webapp.Security.Token.TokenService;
import com.example.task_manager_webapp.Task.TaskRepository;
import com.example.task_manager_webapp.Task.TaskService;
import com.example.task_manager_webapp.User.Logs.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenRepository tokenRepository, TaskRepository  taskRepository, TokenService tokenService, TaskService taskService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.taskRepository  = taskRepository;
        this.taskService = taskService;
        this.tokenService = tokenService;
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

    public Optional<Map<String, Object>> login(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null)
            return Optional.empty();

        Token userToken = tokenService.generateToken(user);

        return Optional.of(
                Map.of(
                        "token", userToken.getToken(),
                        "user", new LoginResponse(
                                user.getUsername(),
                                user.getEmail()
                        )
                )
        );
    }

    public void logout(HttpServletResponse response, String token) {
        // DATABASE
        Optional<User> optionalUser = getUserFromValidToken(token);

        if (optionalUser.isEmpty()) {
            return;
        }

        //DATABASE
        tokenRepository.deleteAllByUser(optionalUser.get());

        // CLIENT-SIDE
        Cookie cookie = new Cookie("authentication-token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    @Transactional
    public boolean deleteUser(String token) {
        Optional<User> userOptional = getUserFromValidToken(token);
        if (userOptional.isEmpty())
            return false;

        User user = userOptional.get();

        // DELETE TASKS AND TOKENS
        taskRepository.deleteAllByUserId(user.getId());
        tokenService.deleteUserTokens(user);

        // DELETE USER
        userRepository.delete(user);

        return true;
    }

    @Transactional
    public boolean updateUser(
            String token,
            String username,
            String email
    ) {
        Optional<User> optionalUser = getUserFromValidToken(token);
        if (optionalUser.isEmpty())
            return false;

        User user = optionalUser.get();

        if (StringUtils.hasText(username) && !username.equals(user.getUsername())) {
            Optional<User> userOptional = userRepository.findUserByUsername(username);

            if (userOptional.isEmpty())
                user.setUsername(username);
        }

        if (StringUtils.hasText(email) && !email.equals(user.getEmail())) {
            Optional<User> userOptional = userRepository.findUserByEmail(email);

            if (userOptional.isEmpty())
                user.setEmail(email);
        }

        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(String token, String oldPassword) {
        Optional<User> userOptional = getUserFromValidToken(token);
        if (userOptional.isEmpty())
            return false;

        User user = userOptional.get();

        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        return true;
    }

    public boolean updateUserPassword(String token, String newPassword) {
        Optional<User> userOptional = getUserFromValidToken(token);
        if (userOptional.isEmpty())
            return false;

        User user = userOptional.get();

        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    private Optional<User> getUserFromValidToken(String token) {
        Optional<Token> userTokenOptional = tokenRepository
                .findByToken(token);

        if (userTokenOptional.isEmpty() || !tokenService.isValidToken(token))
            return Optional.empty();

        return Optional.of(
                userTokenOptional.get().getUser()
        );
    }
}