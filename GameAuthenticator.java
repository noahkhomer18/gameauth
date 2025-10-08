package com.gamingroom.gameauth.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Authenticator implementation for the game authentication system.
 * Validates user credentials and creates GameUser instances for authenticated users.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
public class GameAuthenticator implements Authenticator<BasicCredentials, GameUser> {
    
    // In-memory user store - in production, this would be replaced with a database
    private static final Map<String, UserCredentials> VALID_USERS = new ConcurrentHashMap<>();
    
    static {
        // Initialize with default users
        VALID_USERS.put("guest", new UserCredentials("", ImmutableSet.of()));
        VALID_USERS.put("user", new UserCredentials("password", ImmutableSet.of("USER")));
        VALID_USERS.put("admin", new UserCredentials("admin123", ImmutableSet.of("ADMIN", "USER")));
        VALID_USERS.put("moderator", new UserCredentials("mod456", ImmutableSet.of("MODERATOR", "USER")));
    }

    /**
     * Authenticates a user based on provided credentials.
     * 
     * @param credentials the basic authentication credentials
     * @return Optional containing GameUser if authentication succeeds, empty otherwise
     * @throws AuthenticationException if an error occurs during authentication
     */
    @Override
    public Optional<GameUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            return Optional.empty();
        }
        
        String username = credentials.getUsername().trim();
        String password = credentials.getPassword();
        
        if (username.isEmpty()) {
            return Optional.empty();
        }
        
        try {
            UserCredentials userCreds = VALID_USERS.get(username);
            if (userCreds != null && userCreds.validatePassword(password)) {
                GameUser user = new GameUser(username, userCreds.getRoles());
                return Optional.of(user);
            }
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Adds a new user to the system.
     * 
     * @param username the username
     * @param password the password
     * @param roles the roles for the user
     */
    public static void addUser(String username, String password, Set<String> roles) {
        if (username != null && !username.trim().isEmpty() && password != null) {
            VALID_USERS.put(username.trim(), new UserCredentials(password, roles));
        }
    }
    
    /**
     * Removes a user from the system.
     * 
     * @param username the username to remove
     */
    public static void removeUser(String username) {
        if (username != null) {
            VALID_USERS.remove(username.trim());
        }
    }
    
    /**
     * Internal class to store user credentials and roles.
     */
    private static class UserCredentials {
        private final String password;
        private final Set<String> roles;
        
        public UserCredentials(String password, Set<String> roles) {
            this.password = password;
            this.roles = roles;
        }
        
        public boolean validatePassword(String inputPassword) {
            return password.equals(inputPassword);
        }
        
        public Set<String> getRoles() {
            return roles;
        }
    }
}
