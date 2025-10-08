# GameAuth API Documentation

## Overview

The GameAuth system provides a comprehensive authentication and authorization framework for game applications. This document outlines the API structure and usage patterns.

## Core Classes

### GameUser

The `GameUser` class represents an authenticated user in the system.

#### Constructor
```java
public GameUser(String name, Set<String> roles)
public GameUser(String name)
```

#### Methods
- `String getName()` - Returns the username
- `String getId()` - Returns a unique identifier
- `Set<String> getRoles()` - Returns the set of roles
- `boolean hasRole(String role)` - Checks if user has a specific role
- `boolean hasAnyRole(String... roles)` - Checks if user has any of the specified roles

### GameAuthenticator

Handles user authentication by validating credentials.

#### Methods
- `Optional<GameUser> authenticate(BasicCredentials credentials)` - Authenticates user
- `static void addUser(String username, String password, Set<String> roles)` - Adds new user
- `static void removeUser(String username)` - Removes user

### GameAuthorizer

Manages authorization and permission checking.

#### Methods
- `boolean authorize(GameUser user, String role)` - Checks single role
- `boolean authorizeAny(GameUser user, String... roles)` - Checks any of multiple roles
- `boolean authorizeAll(GameUser user, String... roles)` - Checks all roles
- `boolean isAdmin(GameUser user)` - Checks if user is admin
- `boolean isModeratorOrAdmin(GameUser user)` - Checks if user is moderator or admin

## Usage Examples

### Basic Authentication
```java
GameAuthenticator authenticator = new GameAuthenticator();
BasicCredentials credentials = new BasicCredentials("admin", "admin123");
Optional<GameUser> user = authenticator.authenticate(credentials);
```

### Role-Based Authorization
```java
GameAuthorizer authorizer = new GameAuthorizer();
if (authorizer.isAdmin(user)) {
    // Admin-only functionality
}
```

## Error Handling

All methods include proper null checking and validation. Invalid inputs will return appropriate default values or throw exceptions as documented.
