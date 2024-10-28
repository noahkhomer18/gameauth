# gameauth
This project provides a basic authentication and authorization system for a game application, using Java and Dropwizard. The service includes essential classes to manage user credentials, roles, and permissions, laying a foundation for secure user access.

Files Overview
GameAuthenticator.java:

Implements basic authentication by validating BasicCredentials (username and password).
Uses a hard-coded map of valid users (guest, user, admin) and their respective roles (USER, ADMIN).
Creates a GameUser instance for authenticated users.
GameAuthorizer.java:

Authorizes GameUser instances by verifying their roles.
Uses a role-check method to confirm that a GameUser has the necessary permissions.
GameUser.java:

Represents a user with a name and a set of roles.
Implements the Principal interface to integrate with security frameworks.
Provides methods to retrieve the user’s name, roles, and a randomly generated ID.
How It Works
Authentication: The GameAuthenticator class validates credentials against predefined users and their roles.
Authorization: The GameAuthorizer class verifies if a user has the appropriate role for an action.
