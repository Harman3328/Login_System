package com.todolist.ToDoList.Repository;

/**
 * Used for the retrieval of data from the database
 */

import com.todolist.ToDoList.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Long> {
    UserEntity findByEmail(String email);
}
