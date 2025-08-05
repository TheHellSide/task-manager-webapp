package com.example.to_do_list.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Optional;

@Configuration
public class UserConfiguration {
    @Bean
    CommandLineRunner userCommandLineRunner (UserRepository userRepository){
        return args -> {
            User admin = new User(
                    "admin@admin.com",
                    "Admin",
                    "admin123"
            );

            User joe = new User(
                    "joe@admin.com",
                    "Joe",
                    "joe123"
            );

            for (User user : List.of(admin, joe)){
                if (!userExists(userRepository, user.getEmail())){
                    userRepository.save(user);
                }
            }
        };
    }

    private boolean userExists(UserRepository userRepository, String email){
        Optional<User> userOptional = userRepository
                .findUserByEmail(email);

        return userOptional.isPresent();
    }
}
