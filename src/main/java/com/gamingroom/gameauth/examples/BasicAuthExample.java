package com.gamingroom.gameauth.examples;

import com.gamingroom.gameauth.auth.GameAuthenticator;
import com.gamingroom.gameauth.auth.GameAuthorizer;
import com.gamingroom.gameauth.auth.GameUser;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;
import java.util.Set;

/**
 * Basic authentication example demonstrating how to use the GameAuth system.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
public class BasicAuthExample {

    public static void main(String[] args) {
        System.out.println("=== GameAuth Basic Authentication Example ===\n");

        // Create authenticator and authorizer
        GameAuthenticator authenticator = new GameAuthenticator();
        GameAuthorizer authorizer = new GameAuthorizer();

        // Example 1: Authenticate admin user
        System.out.println("1. Authenticating admin user:");
        BasicCredentials adminCreds = new BasicCredentials("admin", "admin123");
        Optional<GameUser> adminUser;
        try {
            adminUser = authenticator.authenticate(adminCreds);
        } catch (Exception e) {
            System.out.println("   ✗ Authentication error: " + e.getMessage());
            adminUser = Optional.empty();
        }

        if (adminUser.isPresent()) {
            GameUser user = adminUser.get();
            System.out.println("   ✓ Admin authenticated: " + user.getName());
            System.out.println("   ✓ User ID: " + user.getId());
            System.out.println("   ✓ Roles: " + user.getRoles());

            // Check authorization
            if (authorizer.isAdmin(user)) {
                System.out.println("   ✓ User has admin privileges");
            }
        } else {
            System.out.println("   ✗ Authentication failed");
        }

        System.out.println();

        // Example 2: Authenticate regular user
        System.out.println("2. Authenticating regular user:");
        BasicCredentials userCreds = new BasicCredentials("user", "password");
        Optional<GameUser> regularUser;
        try {
            regularUser = authenticator.authenticate(userCreds);
        } catch (Exception e) {
            System.out.println("   ✗ Authentication error: " + e.getMessage());
            regularUser = Optional.empty();
        }

        if (regularUser.isPresent()) {
            GameUser user = regularUser.get();
            System.out.println("   ✓ User authenticated: " + user.getName());
            System.out.println("   ✓ Roles: " + user.getRoles());

            // Check if user can access user features
            if (authorizer.authorize(user, "USER")) {
                System.out.println("   ✓ User has USER role access");
            }

            // Check if user can access admin features
            if (authorizer.isAdmin(user)) {
                System.out.println("   ✓ User has admin privileges");
            } else {
                System.out.println("   ✗ User does not have admin privileges");
            }
        } else {
            System.out.println("   ✗ Authentication failed");
        }

        System.out.println();

        // Example 3: Add new user dynamically
        System.out.println("3. Adding new user dynamically:");
        String newUsername = "newuser";
        String newPassword = "newpass123";
        Set<String> newUserRoles = Set.of("USER", "PLAYER");

        GameAuthenticator.addUser(newUsername, newPassword, newUserRoles);
        System.out.println("   ✓ Added user: " + newUsername);

        // Authenticate the new user
        BasicCredentials newUserCreds = new BasicCredentials(newUsername, newPassword);
        Optional<GameUser> newUser;
        try {
            newUser = authenticator.authenticate(newUserCreds);
        } catch (Exception e) {
            System.out.println("   ✗ Authentication error: " + e.getMessage());
            newUser = Optional.empty();
        }

        if (newUser.isPresent()) {
            GameUser user = newUser.get();
            System.out.println("   ✓ New user authenticated: " + user.getName());
            System.out.println("   ✓ Roles: " + user.getRoles());

            // Check multiple role authorization
            if (authorizer.authorizeAny(user, "USER", "PLAYER")) {
                System.out.println("   ✓ User has USER or PLAYER role");
            }
        }

        System.out.println();

        // Example 4: Failed authentication
        System.out.println("4. Attempting failed authentication:");
        BasicCredentials invalidCreds = new BasicCredentials("invaliduser", "wrongpassword");
        Optional<GameUser> invalidUser;
        try {
            invalidUser = authenticator.authenticate(invalidCreds);
        } catch (Exception e) {
            System.out.println("   ✗ Authentication error: " + e.getMessage());
            invalidUser = Optional.empty();
        }

        if (invalidUser.isEmpty()) {
            System.out.println("   ✓ Correctly rejected invalid credentials");
        } else {
            System.out.println("   ✗ Should have rejected invalid credentials");
        }

        System.out.println();
        System.out.println("=== Example completed successfully ===");
    }
}
