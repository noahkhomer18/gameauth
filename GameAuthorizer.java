package com.gamingroom.gameauth.auth;

import io.dropwizard.auth.Authorizer;

/**
 * Authorizer implementation for the game authentication system.
 * Determines if a user has the necessary permissions to access resources.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
public class GameAuthorizer implements Authorizer<GameUser> {
    
    /**
     * Authorizes a user based on their roles.
     * 
     * @param user the authenticated user
     * @param role the required role for access
     * @return true if the user has the required role, false otherwise
     */
    @Override
    public boolean authorize(GameUser user, String role) {
        if (user == null || role == null || role.trim().isEmpty()) {
            return false;
        }
        
        return user.hasRole(role.trim());
    }
    
    /**
     * Checks if a user has any of the specified roles.
     * 
     * @param user the authenticated user
     * @param roles the required roles (user needs at least one)
     * @return true if the user has any of the required roles, false otherwise
     */
    public boolean authorizeAny(GameUser user, String... roles) {
        if (user == null || roles == null || roles.length == 0) {
            return false;
        }
        
        return user.hasAnyRole(roles);
    }
    
    /**
     * Checks if a user has all of the specified roles.
     * 
     * @param user the authenticated user
     * @param roles the required roles (user needs all of them)
     * @return true if the user has all required roles, false otherwise
     */
    public boolean authorizeAll(GameUser user, String... roles) {
        if (user == null || roles == null || roles.length == 0) {
            return false;
        }
        
        for (String role : roles) {
            if (!user.hasRole(role)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if a user is an administrator.
     * 
     * @param user the authenticated user
     * @return true if the user has ADMIN role, false otherwise
     */
    public boolean isAdmin(GameUser user) {
        return authorize(user, "ADMIN");
    }
    
    /**
     * Checks if a user is a moderator or administrator.
     * 
     * @param user the authenticated user
     * @return true if the user has MODERATOR or ADMIN role, false otherwise
     */
    public boolean isModeratorOrAdmin(GameUser user) {
        return authorizeAny(user, "MODERATOR", "ADMIN");
    }
}