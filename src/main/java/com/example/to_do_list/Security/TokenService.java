package com.example.to_do_list.Security;

import com.example.to_do_list.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public Token generateToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        LocalDateTime creationDate = LocalDateTime.now();

        Token token = new Token(
                tokenString,
                user,
                creationDate
        );
        token.setExpiresAt(creationDate.plusHours(2));
        return tokenRepository.save(token);
    }

    public boolean isValidToken(String tokenStr) {
        Optional<Token> tokenOpt = tokenRepository.findByToken(tokenStr);
        return tokenOpt.isPresent() && tokenOpt.get().getExpiresAt().isAfter(LocalDateTime.now());
    }

    public boolean isTokenValidForUser(String token, Long id) {
        return tokenRepository.existsTokenForUser(token, id);
    }

    public void removeToken(String tokenStr) {
        tokenRepository.deleteByToken(tokenStr);
    }

    @Transient
    public void deleteUserTokens(User user) {
        tokenRepository.deleteAllByUser(user);
    }
}
