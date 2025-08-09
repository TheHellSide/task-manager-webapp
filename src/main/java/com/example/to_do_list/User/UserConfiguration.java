package com.example.to_do_list.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.util.List;
import java.util.Optional;

@Configuration
public class UserConfiguration {
    @Bean
    @Order(1)
    CommandLineRunner userCommandLineRunner (UserRepository userRepository){
        return args -> {
            User admin = new User(
                    "admin@admin.com",
                    "admin",
                    "admin123"
            );

            for (User user : List.of(admin)){
                if (!userExists(userRepository, user.getUsername(), user.getEmail())){
                    userRepository.save(user);
                }
            }
        };
    }

    private boolean userExists(UserRepository userRepository, String username, String email){
        Optional<User> userOptional = userRepository
                .findUserByUsernameOrEmail(username, email);

        return userOptional.isPresent();
    }
}