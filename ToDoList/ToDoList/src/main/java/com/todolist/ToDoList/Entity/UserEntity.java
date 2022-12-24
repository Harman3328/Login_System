package com.todolist.ToDoList.Entity;

import jakarta.persistence.*;

@Entity
@Table (name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private String refreshToken;

    /**
     * gets the id
     * @return long
     */
    public long getId() {
        return id;
    }

    /**
     * gets the email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the email
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * gets the encrypted password
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets the encrypted password
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * gets the refresh JWT
     * @return String
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * sets the refresh JWT
     * @param refreshToken String
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
