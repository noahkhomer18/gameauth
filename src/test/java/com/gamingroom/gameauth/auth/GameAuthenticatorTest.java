package com.gamingroom.gameauth.auth;

import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

/**
 * Unit tests for GameAuthenticator class.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
@DisplayName("GameAuthenticator Tests")
class GameAuthenticatorTest {

    private GameAuthenticator authenticator;

    @BeforeEach
    void setUp() {
        authenticator = new GameAuthenticator();
    }

    @Test
    @DisplayName("Should authenticate valid admin user")
    void shouldAuthenticateValidAdminUser() {
        // Given
        BasicCredentials credentials = new BasicCredentials("admin", "admin123");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isPresent();
        GameUser user = result.get();
        assertThat(user.getName()).isEqualTo("admin");
        assertThat(user.hasRole("ADMIN")).isTrue();
        assertThat(user.hasRole("USER")).isTrue();
    }

    @Test
    @DisplayName("Should authenticate valid regular user")
    void shouldAuthenticateValidRegularUser() {
        // Given
        BasicCredentials credentials = new BasicCredentials("user", "password");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isPresent();
        GameUser user = result.get();
        assertThat(user.getName()).isEqualTo("user");
        assertThat(user.hasRole("USER")).isTrue();
        assertThat(user.hasRole("ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Should authenticate valid moderator user")
    void shouldAuthenticateValidModeratorUser() {
        // Given
        BasicCredentials credentials = new BasicCredentials("moderator", "mod456");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isPresent();
        GameUser user = result.get();
        assertThat(user.getName()).isEqualTo("moderator");
        assertThat(user.hasRole("MODERATOR")).isTrue();
        assertThat(user.hasRole("USER")).isTrue();
    }

    @Test
    @DisplayName("Should authenticate guest user with empty password")
    void shouldAuthenticateGuestUserWithEmptyPassword() {
        // Given
        BasicCredentials credentials = new BasicCredentials("guest", "");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isPresent();
        GameUser user = result.get();
        assertThat(user.getName()).isEqualTo("guest");
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    @DisplayName("Should reject invalid username")
    void shouldRejectInvalidUsername() {
        // Given
        BasicCredentials credentials = new BasicCredentials("invaliduser", "password");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should reject invalid password")
    void shouldRejectInvalidPassword() {
        // Given
        BasicCredentials credentials = new BasicCredentials("admin", "wrongpassword");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should reject null credentials")
    void shouldRejectNullCredentials() {
        // When
        Optional<GameUser> result = authenticator.authenticate(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should reject null username")
    void shouldRejectNullUsername() {
        // Given
        BasicCredentials credentials = new BasicCredentials(null, "password");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should reject null password")
    void shouldRejectNullPassword() {
        // Given
        BasicCredentials credentials = new BasicCredentials("admin", null);

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should reject empty username")
    void shouldRejectEmptyUsername() {
        // Given
        BasicCredentials credentials = new BasicCredentials("", "password");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should trim username whitespace")
    void shouldTrimUsernameWhitespace() {
        // Given
        BasicCredentials credentials = new BasicCredentials("  admin  ", "admin123");

        // When
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should add new user successfully")
    void shouldAddNewUserSuccessfully() {
        // Given
        String username = "newuser";
        String password = "newpass123";
        Set<String> roles = Set.of("USER", "PLAYER");

        // When
        GameAuthenticator.addUser(username, password, roles);
        BasicCredentials credentials = new BasicCredentials(username, password);
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isPresent();
        GameUser user = result.get();
        assertThat(user.getName()).isEqualTo(username);
        assertThat(user.hasRole("USER")).isTrue();
        assertThat(user.hasRole("PLAYER")).isTrue();
    }

    @Test
    @DisplayName("Should remove user successfully")
    void shouldRemoveUserSuccessfully() {
        // Given
        String username = "tempuser";
        String password = "temppass";
        Set<String> roles = Set.of("USER");
        GameAuthenticator.addUser(username, password, roles);

        // When
        GameAuthenticator.removeUser(username);
        BasicCredentials credentials = new BasicCredentials(username, password);
        Optional<GameUser> result = authenticator.authenticate(credentials);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should handle null parameters in addUser")
    void shouldHandleNullParametersInAddUser() {
        // When & Then - should not throw exception
        assertThatCode(() -> {
            GameAuthenticator.addUser(null, "password", Set.of("USER"));
            GameAuthenticator.addUser("user", null, Set.of("USER"));
            GameAuthenticator.addUser("", "password", Set.of("USER"));
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should handle null parameter in removeUser")
    void shouldHandleNullParameterInRemoveUser() {
        // When & Then - should not throw exception
        assertThatCode(() -> GameAuthenticator.removeUser(null))
                .doesNotThrowAnyException();
    }
}
