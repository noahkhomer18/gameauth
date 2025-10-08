package com.gamingroom.gameauth;

import com.gamingroom.gameauth.auth.GameAuthenticator;
import com.gamingroom.gameauth.auth.GameAuthorizer;
import com.gamingroom.gameauth.auth.GameUser;
import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

/**
 * Integration tests for the complete GameAuth system.
 * Tests the interaction between all components.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
@DisplayName("GameAuth Integration Tests")
class GameAuthIntegrationTest {

    private GameAuthenticator authenticator;
    private GameAuthorizer authorizer;

    @BeforeEach
    void setUp() {
        authenticator = new GameAuthenticator();
        authorizer = new GameAuthorizer();
    }

    @Test
    @DisplayName("Should complete full authentication and authorization flow")
    void shouldCompleteFullAuthenticationAndAuthorizationFlow() {
        // Given - Admin credentials
        BasicCredentials adminCredentials = new BasicCredentials("admin", "admin123");

        // When - Authenticate user
        Optional<GameUser> userResult;
        try {
            userResult = authenticator.authenticate(adminCredentials);
        } catch (Exception e) {
            userResult = Optional.empty();
        }

        // Then - User should be authenticated
        assertThat(userResult).isPresent();
        GameUser user = userResult.get();
        assertThat(user.getName()).isEqualTo("admin");
        assertThat(user.getRoles()).contains("ADMIN", "USER");

        // When - Check authorization
        boolean canAccessAdmin = authorizer.authorize(user, "ADMIN");
        boolean canAccessUser = authorizer.authorize(user, "USER");
        boolean isAdmin = authorizer.isAdmin(user);

        // Then - User should have proper permissions
        assertThat(canAccessAdmin).isTrue();
        assertThat(canAccessUser).isTrue();
        assertThat(isAdmin).isTrue();
    }

    @Test
    @DisplayName("Should handle user management operations")
    void shouldHandleUserManagementOperations() {
        // Given - New user details
        String username = "testuser";
        String password = "testpass";
        Set<String> roles = Set.of("USER", "TESTER");

        // When - Add user
        GameAuthenticator.addUser(username, password, roles);

        // Then - User should be able to authenticate
        BasicCredentials credentials = new BasicCredentials(username, password);
        Optional<GameUser> userResult;
        try {
            userResult = authenticator.authenticate(credentials);
        } catch (Exception e) {
            userResult = Optional.empty();
        }
        assertThat(userResult).isPresent();

        GameUser user = userResult.get();
        assertThat(user.getName()).isEqualTo(username);
        assertThat(user.getRoles()).contains("USER", "TESTER");

        // When - Check authorization
        boolean canAccessUser = authorizer.authorize(user, "USER");
        boolean canAccessTester = authorizer.authorize(user, "TESTER");
        boolean canAccessAdmin = authorizer.authorize(user, "ADMIN");

        // Then - User should have correct permissions
        assertThat(canAccessUser).isTrue();
        assertThat(canAccessTester).isTrue();
        assertThat(canAccessAdmin).isFalse();

        // When - Remove user
        GameAuthenticator.removeUser(username);

        // Then - User should not be able to authenticate
        Optional<GameUser> removedUserResult;
        try {
            removedUserResult = authenticator.authenticate(credentials);
        } catch (Exception e) {
            removedUserResult = Optional.empty();
        }
        assertThat(removedUserResult).isEmpty();
    }

    @Test
    @DisplayName("Should handle complex authorization scenarios")
    void shouldHandleComplexAuthorizationScenarios() {
        // Given - Multiple users with different roles
        GameAuthenticator.addUser("superuser", "superpass", Set.of("SUPER_USER", "ADMIN", "USER"));
        GameAuthenticator.addUser("moderator", "modpass", Set.of("MODERATOR", "USER"));
        GameAuthenticator.addUser("player", "playerpass", Set.of("PLAYER", "USER"));

        // Test super user
        Optional<GameUser> superUser;
        try {
            superUser = authenticator.authenticate(new BasicCredentials("superuser", "superpass"));
        } catch (Exception e) {
            superUser = Optional.empty();
        }
        assertThat(superUser).isPresent();
        assertThat(authorizer.authorizeAll(superUser.get(), "SUPER_USER", "ADMIN", "USER")).isTrue();
        assertThat(authorizer.isAdmin(superUser.get())).isTrue();

        // Test moderator
        Optional<GameUser> moderator;
        try {
            moderator = authenticator.authenticate(new BasicCredentials("moderator", "modpass"));
        } catch (Exception e) {
            moderator = Optional.empty();
        }
        assertThat(moderator).isPresent();
        assertThat(authorizer.authorizeAny(moderator.get(), "MODERATOR", "ADMIN")).isTrue();
        assertThat(authorizer.isModeratorOrAdmin(moderator.get())).isTrue();
        assertThat(authorizer.isAdmin(moderator.get())).isFalse();

        // Test player
        Optional<GameUser> player;
        try {
            player = authenticator.authenticate(new BasicCredentials("player", "playerpass"));
        } catch (Exception e) {
            player = Optional.empty();
        }
        assertThat(player).isPresent();
        assertThat(authorizer.authorize(player.get(), "PLAYER")).isTrue();
        assertThat(authorizer.authorize(player.get(), "ADMIN")).isFalse();
        assertThat(authorizer.isModeratorOrAdmin(player.get())).isFalse();

        // Cleanup
        GameAuthenticator.removeUser("superuser");
        GameAuthenticator.removeUser("moderator");
        GameAuthenticator.removeUser("player");
    }

    @Test
    @DisplayName("Should handle security edge cases")
    void shouldHandleSecurityEdgeCases() {
        // Test null and empty inputs
        Optional<GameUser> nullResult, emptyResult, emptyPassResult;
        try {
            nullResult = authenticator.authenticate(null);
        } catch (Exception e) {
            nullResult = Optional.empty();
        }
        try {
            emptyResult = authenticator.authenticate(new BasicCredentials("", "password"));
        } catch (Exception e) {
            emptyResult = Optional.empty();
        }
        try {
            emptyPassResult = authenticator.authenticate(new BasicCredentials("user", ""));
        } catch (Exception e) {
            emptyPassResult = Optional.empty();
        }
        assertThat(nullResult).isEmpty();
        assertThat(emptyResult).isEmpty();
        assertThat(emptyPassResult).isEmpty();

        // Test authorization with null inputs
        Optional<GameUser> validUser;
        try {
            validUser = authenticator.authenticate(new BasicCredentials("user", "password"));
        } catch (Exception e) {
            validUser = Optional.empty();
        }
        assertThat(validUser).isPresent();
        assertThat(authorizer.authorize(null, "USER")).isFalse();
        assertThat(authorizer.authorize(validUser.get(), null)).isFalse();
        assertThat(authorizer.authorize(validUser.get(), "")).isFalse();
    }

    @Test
    @DisplayName("Should maintain thread safety")
    void shouldMaintainThreadSafety() throws InterruptedException {
        // Given - Multiple threads trying to authenticate simultaneously
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        boolean[] results = new boolean[threadCount];

        // When - Start multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
            BasicCredentials credentials = new BasicCredentials("user", "password");
            Optional<GameUser> result;
            try {
                result = authenticator.authenticate(credentials);
            } catch (Exception e) {
                result = Optional.empty();
            }
                results[index] = result.isPresent();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Then - All authentications should succeed
        for (boolean result : results) {
            assertThat(result).isTrue();
        }
    }
}
