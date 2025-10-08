package com.gamingroom.gameauth.auth;

import java.security.Principal;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user in the game authentication system.
 * Implements Principal interface for integration with security frameworks.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
public class GameUser implements Principal {
    private final String name;
    private final Set<String> roles;
    private final String id;

    /**
     * Constructor for creating a user with roles.
     * 
     * @param name the username
     * @param roles the set of roles assigned to this user
     * @throws IllegalArgumentException if name is null or empty
     */
    public GameUser(String name, Set<String> roles) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.name = name.trim();
        this.roles = roles;
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constructor for creating a user without roles.
     * 
     * @param name the username
     * @throws IllegalArgumentException if name is null or empty
     */
    public GameUser(String name) {
        this(name, null);
    }

    /**
     * Gets the username.
     * 
     * @return the username
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets a unique identifier for this user.
     * 
     * @return a unique string identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the roles assigned to this user.
     * 
     * @return the set of roles, or null if no roles are assigned
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Checks if this user has a specific role.
     * 
     * @param role the role to check for
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    /**
     * Checks if this user has any of the specified roles.
     * 
     * @param requiredRoles the roles to check for
     * @return true if the user has at least one of the roles, false otherwise
     */
    public boolean hasAnyRole(String... requiredRoles) {
        if (roles == null || requiredRoles == null) {
            return false;
        }
        for (String role : requiredRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameUser gameUser = (GameUser) obj;
        return Objects.equals(name, gameUser.name) && Objects.equals(id, gameUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "GameUser{" +
                "name='" + name + '\'' +
                ", roles=" + roles +
                ", id='" + id + '\'' +
                '}';
    }
}