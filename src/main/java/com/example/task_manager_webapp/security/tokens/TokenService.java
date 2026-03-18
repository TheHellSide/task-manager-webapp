package com.example.task_manager_webapp.security.tokens;

import static com.example.task_manager_webapp.security.Security.sha256;
import com.example.task_manager_webapp.users.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.beans.Transient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        Token token = new Token(
                sha256(tokenString),
                user,
                creationDate
        );
        token.setExpiresAt(creationDate.plusHours(24 * 30)); // 24H * 30
        tokenRepository.save(token);

        return tokenString;
    }

    // TESTING FUNCTION
    public boolean extendSession(String token, HttpServletResponse response, int timeInMinutes) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(
                sha256(token)
        );

        if (tokenOpt.isEmpty()) {
            return false;
        }

        Token existingToken = tokenOpt.get();
        if (existingToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        existingToken.setExpiresAt(existingToken.getExpiresAt().plusMinutes(timeInMinutes));
        tokenRepository.save(existingToken);

        Cookie cookie = new Cookie("authentication-token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS = true
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.between(LocalDateTime.now(), existingToken.getExpiresAt()).getSeconds());

        response.addCookie(cookie);
        return true;
    }

    public boolean isValidToken(String tokenStr) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(
                sha256(tokenStr)
        );
        return tokenOpt.isPresent() && tokenOpt.get().getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Transient
    public void deleteUserTokens(User user) {
        tokenRepository.deleteAllByUser(user);
    }
}
