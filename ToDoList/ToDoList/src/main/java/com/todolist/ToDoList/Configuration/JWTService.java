package com.todolist.ToDoList.Configuration;
/**
 * Class that holds all JWT operations such as;
 * generating a JWT,
 * generating a refresh JWT,
 * checking if a JWT is expired,
 * getting email from a JWT
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    // algorithm for the JWT, should be in a different file but for development purposes, it is placed here
    private final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    /**
     * generates a JWT with the subject an the client email
     * @param email String
     * @return String
     */
    public String generateJWT(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(algorithm);
    }

    /**
     * checks to see if a JWT is expired
     * if it is, return false
     * otherwise, return true
     * @param jwt String
     * @return boolean
     */
    public boolean isTokenExpired(String jwt) {
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            return false;
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    /**
     * Generates a refresh JWT
     * used for when the JWT is expired and a server needs to send a new one to the client
     * @param email String
     * @return String
     */
    public String generateFreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .sign(algorithm);
    }

    /**
     * Generates an expired JWT
     * used for when the user signs out
     * @param email String
     * @return String
     */
    public String getExpiredJWT(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() / 1000L - 3600L))
                .sign(algorithm);
    }

    /**
     * gets the email from a JWT
     * @param jwt String
     * @return String
     */
    public String getEmail(String jwt) {
        return JWT.decode(jwt)
                .getSubject();
    }
}
