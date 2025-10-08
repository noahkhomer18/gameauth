# GameAuth - Game Authentication & Authorization System

A robust Java-based authentication and authorization system designed for game applications, built with Dropwizard framework. This project provides secure user management with role-based access control, perfect for gaming platforms that require user authentication and permission management.

## 🚀 Features

- **Secure Authentication**: Basic authentication with username/password validation
- **Role-Based Authorization**: Flexible role system supporting multiple user types
- **Thread-Safe Operations**: Concurrent user management with thread-safe data structures
- **Comprehensive Error Handling**: Robust error handling and validation
- **Extensible Design**: Easy to integrate with existing game applications
- **Production-Ready**: Enhanced security practices and proper documentation

## 📋 Prerequisites

- Java 8 or higher
- Maven 3.6+ (for building)
- Dropwizard framework
- Basic understanding of authentication concepts

## 🏗️ Project Structure

```
gameauth/
├── GameUser.java          # User entity with roles and permissions
├── GameAuthenticator.java # Authentication logic and user validation
├── GameAuthorizer.java    # Authorization and permission checking
├── README.md             # Project documentation
└── pom.xml               # Maven build configuration
```

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd gameauth
```

### 2. Build the Project
```bash
mvn clean compile
```

### 3. Run Tests
```bash
mvn test
```

## 📚 API Documentation

### GameUser Class

The `GameUser` class represents an authenticated user in the system.

#### Key Methods:
- `getName()`: Returns the username
- `getId()`: Returns a unique identifier
- `getRoles()`: Returns the set of roles assigned to the user
- `hasRole(String role)`: Checks if user has a specific role
- `hasAnyRole(String... roles)`: Checks if user has any of the specified roles

#### Example Usage:
```java
GameUser user = new GameUser("admin", Set.of("ADMIN", "USER"));
boolean isAdmin = user.hasRole("ADMIN");
boolean hasPermission = user.hasAnyRole("USER", "MODERATOR");
```

### GameAuthenticator Class

Handles user authentication by validating credentials.

#### Default Users:
- **guest**: No password, no roles
- **user**: Password: "password", Role: USER
- **admin**: Password: "admin123", Roles: ADMIN, USER
- **moderator**: Password: "mod456", Roles: MODERATOR, USER

#### Key Methods:
- `authenticate(BasicCredentials)`: Authenticates user credentials
- `addUser(String, String, Set<String>)`: Adds a new user to the system
- `removeUser(String)`: Removes a user from the system

#### Example Usage:
```java
BasicCredentials creds = new BasicCredentials("admin", "admin123");
Optional<GameUser> user = authenticator.authenticate(creds);

// Add a new user
GameAuthenticator.addUser("newuser", "password123", Set.of("USER"));
```

### GameAuthorizer Class

Manages authorization and permission checking.

#### Key Methods:
- `authorize(GameUser, String)`: Checks if user has a specific role
- `authorizeAny(GameUser, String...)`: Checks if user has any of the roles
- `authorizeAll(GameUser, String...)`: Checks if user has all roles
- `isAdmin(GameUser)`: Checks if user is an administrator
- `isModeratorOrAdmin(GameUser)`: Checks if user is moderator or admin

#### Example Usage:
```java
GameAuthorizer authorizer = new GameAuthorizer();

// Check single role
boolean canAccess = authorizer.authorize(user, "USER");

// Check multiple roles (any)
boolean canModerate = authorizer.authorizeAny(user, "MODERATOR", "ADMIN");

// Check multiple roles (all)
boolean isSuperUser = authorizer.authorizeAll(user, "ADMIN", "SUPER_USER");
```

## 🔐 Security Features

### Authentication Security
- Input validation and sanitization
- Null and empty string checks
- Secure credential storage (in-memory for demo)
- Exception handling for authentication failures

### Authorization Security
- Role-based access control
- Flexible permission checking
- Null-safe operations
- Comprehensive validation

## 🧪 Testing

The project includes comprehensive unit tests to ensure reliability:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=GameUserTest

# Generate test coverage report
mvn jacoco:report
```

## 🚀 Usage Examples

### Basic Authentication Flow
```java
// 1. Create authenticator
GameAuthenticator authenticator = new GameAuthenticator();

// 2. Authenticate user
BasicCredentials credentials = new BasicCredentials("admin", "admin123");
Optional<GameUser> user = authenticator.authenticate(credentials);

if (user.isPresent()) {
    GameUser authenticatedUser = user.get();
    System.out.println("User authenticated: " + authenticatedUser.getName());
    
    // 3. Check authorization
    GameAuthorizer authorizer = new GameAuthorizer();
    if (authorizer.isAdmin(authenticatedUser)) {
        System.out.println("User has admin privileges");
    }
}
```

### Role-Based Access Control
```java
// Check if user can access admin features
if (authorizer.authorize(user, "ADMIN")) {
    // Allow access to admin features
}

// Check if user can moderate content
if (authorizer.isModeratorOrAdmin(user)) {
    // Allow moderation features
}
```

## 🔧 Configuration

### Adding New Users
```java
// Add a new user with specific roles
GameAuthenticator.addUser("newuser", "securepassword", Set.of("USER", "PLAYER"));
```

### Custom Roles
The system supports any custom roles. Common roles include:
- `USER`: Basic user access
- `ADMIN`: Administrative privileges
- `MODERATOR`: Content moderation rights
- `PLAYER`: Game-specific permissions

## 🛠️ Development

### Building from Source
```bash
# Clean and compile
mvn clean compile

# Package the application
mvn package

# Run with Maven
mvn exec:java -Dexec.mainClass="com.gamingroom.gameauth.GameAuthApplication"
```

### Code Quality
The project follows Java best practices:
- Comprehensive JavaDoc documentation
- Input validation and error handling
- Thread-safe implementations
- Clean, readable code structure

## 📈 Performance Considerations

- **Thread Safety**: Uses `ConcurrentHashMap` for thread-safe user storage
- **Memory Efficiency**: Immutable user objects and efficient data structures
- **Scalability**: Designed for easy integration with database backends

## 🔮 Future Enhancements

- [ ] Database integration for persistent user storage
- [ ] Password hashing and encryption
- [ ] JWT token support
- [ ] OAuth2 integration
- [ ] Rate limiting and security policies
- [ ] Audit logging
- [ ] Multi-factor authentication

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **GameAuth Team** - *Initial work* - [GitHub](https://github.com/gameauth)

## 🙏 Acknowledgments

- SNHU Computer Science Program
- Dropwizard Framework Team
- Java Community for best practices and patterns

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation for common solutions

---

**Note**: This is a demonstration project created for educational purposes. For production use, implement additional security measures including password hashing, database integration, and comprehensive security auditing.
