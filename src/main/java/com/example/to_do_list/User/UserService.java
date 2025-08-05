package com.example.to_do_list.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    //TODO
    public boolean registerNewUser(User user) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository
                .findByUsernameOrEmail(user.getUsername(), user.getEmail()));

        if (optionalUser.isEmpty()){
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public boolean deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);

        if (exists) {
            userRepository.deleteById(userId);
            return true;
        }

        return false;
    }

    public boolean updateUser(
            Long userId,
            String username,
            String email
    ) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (username != null && !username.isBlank() && !username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
            user.setEmail(email);
        }

        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(Long userId, String oldPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false; // User not found
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }

        return true;
    }

    public boolean updateUserPassword(Long userId, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false; // User not found
        }

        User user = optionalUser.get();

        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }
}
