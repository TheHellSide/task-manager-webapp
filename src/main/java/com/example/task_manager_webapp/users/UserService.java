package com.example.task_manager_webapp.users;

import static com.example.task_manager_webapp.security.Security.sha256;
import com.example.task_manager_webapp.security.tokens.Token;
import com.example.task_manager_webapp.security.tokens.TokenRepository;
import com.example.task_manager_webapp.security.tokens.TokenService;
import com.example.task_manager_webapp.tasks.TaskRepository;
import com.example.task_manager_webapp.tasks.TaskService;
import com.example.task_manager_webapp.users.dto.login.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final BCryptPasswordEncoder encoder; // TO IMPLEMENT.
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final TokenService tokenService;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TokenRepository tokenRepository, TaskRepository  taskRepository, TokenService tokenService, TaskService taskService) {
        this.encoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.taskRepository  = taskRepository;
        this.taskService = taskService;
        this.tokenService = tokenService;
    }

    public boolean registerNewUser(User user) {
        Optional<User> optionalUser = userRepository
                .findByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (optionalUser.isEmpty()){
            user.setPassword(
                    encoder.encode(
                            user.getPassword()
                    ));

            userRepository.save(user);
            taskService.createDefaultTask(user);

            return true;
        }

        return false;
    }

    public Optional<Map<String, Object>> login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            return Optional.empty();

        User user = userOptional.get();
        if (!encoder.matches(password, user.getPassword()))
            return Optional.empty();

        String unhashed_token = tokenService.generateToken(user);

        return Optional.of(
                Map.of(
                        "token", unhashed_token,
                        "user", new LoginResponse(
                                user.getUsername(),
                                user.getEmail()
                        )
                )
        );
    }

    public void logout(HttpServletResponse response, String token) {
        // DATABASE
        Optional<User> optionalUser = getUserFromValidToken(
                sha256(token)
        );

        if (optionalUser.isEmpty()) {
            return;
        }

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
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isEmpty())
                user.setUsername(username);
        }

        if (StringUtils.hasText(email) && !email.equals(user.getEmail())) {
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty())
                user.setEmail(email);
        }

        userRepository.save(user);
        return true;
    }

    public User checkPassword(String token, String oldPassword) {
        Optional<User> userOptional = getUserFromValidToken(token);
        if (userOptional.isEmpty())
            return null;

        User user = userOptional.get();

        if (!encoder.matches(oldPassword, user.getPassword())) {
            return null;
        }

        return user;
    }

    public boolean updateUserPassword(String token, String oldPassword, String newPassword) {
        User user = checkPassword(token, oldPassword);

        if (user == null)
            return false;

        user.setPassword(
                encoder.encode(newPassword)
        );
        
        userRepository.save(user);
        return true;
    }

    private Optional<User> getUserFromValidToken(String token) {
        Optional<Token> userTokenOptional = tokenRepository
                .findByToken(
                        sha256(token)
                );

        if (userTokenOptional.isEmpty() || !tokenService.isValidToken(token))
            return Optional.empty();

        return Optional.of(
                userTokenOptional.get().getUser()
        );
    }
}