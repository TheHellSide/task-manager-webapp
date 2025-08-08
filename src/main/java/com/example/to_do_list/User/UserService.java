package com.example.to_do_list.User;

import com.example.to_do_list.Security.Token;
import com.example.to_do_list.Security.TokenService;
import com.example.to_do_list.User.Logs.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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
            return true;
        }

        return false;
    }

    //TODO: ADD TOKEN HANDLING.
    public boolean deleteUser(String token, Long userId) {
        boolean exists = userRepository.existsById(userId);

        if (
                exists &&
                tokenService.isValidToken(token) &&
                tokenService.isTokenValidForUser(token, userId)
        ) {
            userRepository.deleteById(userId);
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