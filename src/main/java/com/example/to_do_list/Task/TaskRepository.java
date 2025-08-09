package com.example.to_do_list.Task;

import com.example.to_do_list.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.user = :user")
    List<Task> findAllByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}