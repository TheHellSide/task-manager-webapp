package com.example.to_do_list.Security;

import com.example.to_do_list.User.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUser(User user);
    void deleteByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t WHERE t.user = :user")
    void deleteAllByUser(@Param("user") User user);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Token t WHERE t.token = :token AND t.user.id = :id")
    boolean existsTokenForUser(@Param("token") String token, @Param("id") Long id);
}