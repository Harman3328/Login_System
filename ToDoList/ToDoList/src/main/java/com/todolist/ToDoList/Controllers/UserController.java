package com.todolist.ToDoList.Controllers;

/**
 * Handel's all user requests such as
 * logins,
 * logouts,
 * creating accounts,
 * checking to see if the user is logged in via JWT
 */

import com.todolist.ToDoList.Configuration.JWTService;
import com.todolist.ToDoList.Entity.UserEntity;
import com.todolist.ToDoList.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    /*@GetMapping(path = "/all")
    public @ResponseBody List<UserEntity> getAll() {
        return userRepository.findAll();
    }*/

    /**
     * Checks to see if the email given by the client exists in the database
     * if it exists, sends a boolean value of false, indicating account cannot be made
     * otherwise, it encrypts the password and stores both the email and password in the database
     * @param userEntity UserEntity
     * @return boolean
     */
    @PostMapping(path = "/add")
    public @ResponseBody boolean addNewUser(@RequestBody UserEntity userEntity) {
        if (getUser(userEntity.getEmail()) != null) {
            return false;
        } else {
            String password = userEntity.getPassword();
            userEntity.setPassword(this.passwordEncoder.encode(password));
            userRepository.save(userEntity);
            return true;
        }
    }

    /**
     * checks to see if email and its corresponding encrypted password exists in the database
     * if true, creates a JWT and a refresh JWT
     * the JWT is sent back to the client while the refresh JWT is stored in the database
     * if false, sends an unauthorized error back
     * @param userEntity UserEntity
     * @return ResponseEntity
     */
    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<Map<String, String>> userLogin(@RequestBody UserEntity userEntity) {
        UserEntity dbUser = getUser(userEntity.getEmail());
        if (dbUser != null && passwordEncoder.matches(userEntity.getPassword(),
                getEncodedPassword(userEntity.getEmail()))) {
            String jwt = jwtService.generateJWT(userEntity.getEmail());
            String refreshToken = jwtService.generateFreshToken(userEntity.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("jwt", jwt);
            dbUser.setRefreshToken(refreshToken);
            userRepository.save(dbUser);
            return ResponseEntity.ok(response);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Checks to see if the JWT sent by the client is still valid
     * if it is valid, return true
     * otherwise, return false
     * @param jwtToken String
     * @return boolean
     */
    @PostMapping(path = "/auth")
    public @ResponseBody boolean jwtAuth(@RequestBody String jwtToken) {
        return !jwtService.isTokenExpired(jwtToken);
    }

    /**
     * gets the user email from the JWT
     * Using the email, gets the refresh JWT from the database
     * checks to see it the refresh JWT is valid or not
     * if it is valid, create a new JWT and send it to the client along with an indicator signifying the client is
     * still logged in
     * otherwise, signify the client is no longer logged in
     * if there is an exception, send Not_found status back
     * @param jwtToken String
     * @return ResponseEntity
     */
    @PostMapping(path = "/checkrefreshtoken")
    public @ResponseBody ResponseEntity<Map<String, String>> checkRefreshToken(@RequestBody String jwtToken) {
        try {
            UserEntity userEntity = getUser(jwtService.getEmail(jwtToken));
            Map<String, String> response = new HashMap<>();
            if (!jwtService.isTokenExpired(userEntity.getRefreshToken())) {
                String jwt = jwtService.generateJWT(userEntity.getEmail());
                response.put("loggedIn", "true");
                response.put("jwt", jwt);
            } else {
                response.put("loggedIn", "false");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get email from JWT
     * set the refresh JWT in the database to an expired one
     * send an expired JWT back to the client
     * @param jwtToken String
     * @return ResponseEntity
     */
    @PostMapping(path = "/logout")
    public @ResponseBody ResponseEntity<Map<String, String>> logout(@RequestBody String jwtToken) {
        String email = jwtService.getEmail(jwtToken);
        UserEntity userEntity = getUser(email);
        String expiredJWT = jwtService.getExpiredJWT(userEntity.getEmail());
        userEntity.setRefreshToken(expiredJWT);
        userRepository.save(userEntity);
        Map<String, String> response = new HashMap<>();
        response.put("loggedOut", expiredJWT);
        return ResponseEntity.ok(response);
    }

    /**
     * get UserEntity from email
     * @param email String
     * @return UserEntity
     */
    private UserEntity getUser(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Gets the encrypted password of the corresponding email
     * @param email String
     * @return String
     */
    private String getEncodedPassword(String email) {
        return userRepository.findByEmail(email).getPassword();
    }
}
