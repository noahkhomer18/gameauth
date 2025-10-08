package com.gamingroom.gameauth.examples;

import com.gamingroom.gameauth.auth.GameAuthenticator;
import com.gamingroom.gameauth.auth.GameAuthorizer;
import com.gamingroom.gameauth.auth.GameUser;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;
import java.util.Set;

/**
 * Advanced authentication example demonstrating complex authorization scenarios.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
public class AdvancedAuthExample {
    
    public static void main(String[] args) {
        System.out.println("=== GameAuth Advanced Authentication Example ===\n");
        
        // Create authenticator and authorizer
        GameAuthenticator authenticator = new GameAuthenticator();
        GameAuthorizer authorizer = new GameAuthorizer();
        
        // Setup test users with different roles
        setupTestUsers();
        
        // Example 1: Role hierarchy demonstration
        System.out.println("1. Role Hierarchy Demonstration:");
        demonstrateRoleHierarchy(authenticator, authorizer);
        
        // Example 2: Complex authorization scenarios
        System.out.println("\n2. Complex Authorization Scenarios:");
        demonstrateComplexAuthorization(authenticator, authorizer);
        
        // Example 3: User management operations
        System.out.println("\n3. User Management Operations:");
        demonstrateUserManagement(authenticator, authorizer);
        
        // Example 4: Security edge cases
        System.out.println("\n4. Security Edge Cases:");
        demonstrateSecurityEdgeCases(authenticator, authorizer);
        
        System.out.println("\n=== Advanced example completed successfully ===");
    }
    
    private static void setupTestUsers() {
        // Add some test users with specific roles
        GameAuthenticator.addUser("superadmin", "superpass", Set.of("SUPER_ADMIN", "ADMIN", "USER"));
        GameAuthenticator.addUser("moderator1", "modpass1", Set.of("MODERATOR", "USER"));
        GameAuthenticator.addUser("player1", "playerpass1", Set.of("PLAYER", "USER"));
        GameAuthenticator.addUser("guest", "guestpass", Set.of("GUEST"));
    }
    
    private static void demonstrateRoleHierarchy(GameAuthenticator authenticator, GameAuthorizer authorizer) {
        // Test super admin
        Optional<GameUser> superAdmin = authenticator.authenticate(new BasicCredentials("superadmin", "superpass"));
        if (superAdmin.isPresent()) {
            GameUser user = superAdmin.get();
            System.out.println("   Super Admin: " + user.getName());
            System.out.println("   ✓ Has SUPER_ADMIN role: " + authorizer.authorize(user, "SUPER_ADMIN"));
            System.out.println("   ✓ Has ADMIN role: " + authorizer.authorize(user, "ADMIN"));
            System.out.println("   ✓ Has USER role: " + authorizer.authorize(user, "USER"));
            System.out.println("   ✓ Is admin: " + authorizer.isAdmin(user));
        }
        
        // Test moderator
        Optional<GameUser> moderator = authenticator.authenticate(new BasicCredentials("moderator1", "modpass1"));
        if (moderator.isPresent()) {
            GameUser user = moderator.get();
            System.out.println("   Moderator: " + user.getName());
            System.out.println("   ✓ Has MODERATOR role: " + authorizer.authorize(user, "MODERATOR"));
            System.out.println("   ✓ Has USER role: " + authorizer.authorize(user, "USER"));
            System.out.println("   ✓ Is moderator or admin: " + authorizer.isModeratorOrAdmin(user));
            System.out.println("   ✗ Is admin: " + authorizer.isAdmin(user));
        }
    }
    
    private static void demonstrateComplexAuthorization(GameAuthenticator authenticator, GameAuthorizer authorizer) {
        // Test player with multiple roles
        Optional<GameUser> player = authenticator.authenticate(new BasicCredentials("player1", "playerpass1"));
        if (player.isPresent()) {
            GameUser user = player.get();
            System.out.println("   Player: " + user.getName());
            
            // Test any role authorization
            System.out.println("   ✓ Has USER or PLAYER role: " + 
                authorizer.authorizeAny(user, "USER", "PLAYER"));
            System.out.println("   ✓ Has PLAYER or MODERATOR role: " + 
                authorizer.authorizeAny(user, "PLAYER", "MODERATOR"));
            System.out.println("   ✗ Has ADMIN or MODERATOR role: " + 
                authorizer.authorizeAny(user, "ADMIN", "MODERATOR"));
            
            // Test all roles authorization
            System.out.println("   ✓ Has both USER and PLAYER roles: " + 
                authorizer.authorizeAll(user, "USER", "PLAYER"));
            System.out.println("   ✗ Has both USER and ADMIN roles: " + 
                authorizer.authorizeAll(user, "USER", "ADMIN"));
        }
    }
    
    private static void demonstrateUserManagement(GameAuthenticator authenticator, GameAuthorizer authorizer) {
        // Add a temporary user
        String tempUser = "tempuser";
        String tempPass = "temppass";
        Set<String> tempRoles = Set.of("USER", "TESTER");
        
        System.out.println("   Adding temporary user: " + tempUser);
        GameAuthenticator.addUser(tempUser, tempPass, tempRoles);
        
        // Verify the user was added
        Optional<GameUser> addedUser = authenticator.authenticate(new BasicCredentials(tempUser, tempPass));
        if (addedUser.isPresent()) {
            System.out.println("   ✓ Temporary user authenticated successfully");
            System.out.println("   ✓ Roles: " + addedUser.get().getRoles());
        }
        
        // Remove the temporary user
        System.out.println("   Removing temporary user: " + tempUser);
        GameAuthenticator.removeUser(tempUser);
        
        // Verify the user was removed
        Optional<GameUser> removedUser = authenticator.authenticate(new BasicCredentials(tempUser, tempPass));
        if (removedUser.isEmpty()) {
            System.out.println("   ✓ Temporary user successfully removed");
        } else {
            System.out.println("   ✗ Temporary user still exists");
        }
    }
    
    private static void demonstrateSecurityEdgeCases(GameAuthenticator authenticator, GameAuthorizer authorizer) {
        System.out.println("   Testing null and empty inputs:");
        
        // Test null credentials
        Optional<GameUser> nullResult = authenticator.authenticate(null);
        System.out.println("   ✓ Null credentials handled: " + (nullResult.isEmpty() ? "PASS" : "FAIL"));
        
        // Test empty username
        Optional<GameUser> emptyUserResult = authenticator.authenticate(new BasicCredentials("", "password"));
        System.out.println("   ✓ Empty username handled: " + (emptyUserResult.isEmpty() ? "PASS" : "FAIL"));
        
        // Test null username
        Optional<GameUser> nullUserResult = authenticator.authenticate(new BasicCredentials(null, "password"));
        System.out.println("   ✓ Null username handled: " + (nullUserResult.isEmpty() ? "PASS" : "FAIL"));
        
        // Test authorization with null user
        boolean nullUserAuth = authorizer.authorize(null, "USER");
        System.out.println("   ✓ Null user authorization handled: " + (!nullUserAuth ? "PASS" : "FAIL"));
        
        // Test authorization with null role
        Optional<GameUser> validUser = authenticator.authenticate(new BasicCredentials("user", "password"));
        if (validUser.isPresent()) {
            boolean nullRoleAuth = authorizer.authorize(validUser.get(), null);
            System.out.println("   ✓ Null role authorization handled: " + (!nullRoleAuth ? "PASS" : "FAIL"));
        }
    }
}
