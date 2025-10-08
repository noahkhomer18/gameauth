package com.gamingroom.gameauth.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

/**
 * Unit tests for GameAuthorizer class.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
@DisplayName("GameAuthorizer Tests")
class GameAuthorizerTest {

    private GameAuthorizer authorizer;
    private GameUser adminUser;
    private GameUser regularUser;
    private GameUser moderatorUser;
    private GameUser userWithNoRoles;

    @BeforeEach
    void setUp() {
        authorizer = new GameAuthorizer();
        adminUser = new GameUser("admin", Set.of("ADMIN", "USER"));
        regularUser = new GameUser("user", Set.of("USER"));
        moderatorUser = new GameUser("moderator", Set.of("MODERATOR", "USER"));
        userWithNoRoles = new GameUser("guest", Set.of());
    }

    @Test
    @DisplayName("Should authorize user with correct role")
    void shouldAuthorizeUserWithCorrectRole() {
        // When & Then
        assertThat(authorizer.authorize(adminUser, "ADMIN")).isTrue();
        assertThat(authorizer.authorize(regularUser, "USER")).isTrue();
        assertThat(authorizer.authorize(moderatorUser, "MODERATOR")).isTrue();
    }

    @Test
    @DisplayName("Should not authorize user without required role")
    void shouldNotAuthorizeUserWithoutRequiredRole() {
        // When & Then
        assertThat(authorizer.authorize(regularUser, "ADMIN")).isFalse();
        assertThat(authorizer.authorize(regularUser, "MODERATOR")).isFalse();
        assertThat(authorizer.authorize(adminUser, "SUPER_USER")).isFalse();
    }

    @Test
    @DisplayName("Should not authorize null user")
    void shouldNotAuthorizeNullUser() {
        // When & Then
        assertThat(authorizer.authorize(null, "USER")).isFalse();
        assertThat(authorizer.authorize(null, "ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Should not authorize with null role")
    void shouldNotAuthorizeWithNullRole() {
        // When & Then
        assertThat(authorizer.authorize(adminUser, null)).isFalse();
        assertThat(authorizer.authorize(regularUser, null)).isFalse();
    }

    @Test
    @DisplayName("Should not authorize with empty role")
    void shouldNotAuthorizeWithEmptyRole() {
        // When & Then
        assertThat(authorizer.authorize(adminUser, "")).isFalse();
        assertThat(authorizer.authorize(adminUser, "   ")).isFalse();
    }

    @Test
    @DisplayName("Should trim role whitespace")
    void shouldTrimRoleWhitespace() {
        // When & Then
        assertThat(authorizer.authorize(adminUser, "  ADMIN  ")).isTrue();
        assertThat(authorizer.authorize(regularUser, "  USER  ")).isTrue();
    }

    @Test
    @DisplayName("Should authorize any role when user has one of them")
    void shouldAuthorizeAnyRoleWhenUserHasOneOfThem() {
        // When & Then
        assertThat(authorizer.authorizeAny(adminUser, "ADMIN", "MODERATOR")).isTrue();
        assertThat(authorizer.authorizeAny(regularUser, "USER", "ADMIN")).isTrue();
        assertThat(authorizer.authorizeAny(moderatorUser, "MODERATOR", "SUPER_USER")).isTrue();
    }

    @Test
    @DisplayName("Should not authorize any role when user has none of them")
    void shouldNotAuthorizeAnyRoleWhenUserHasNoneOfThem() {
        // When & Then
        assertThat(authorizer.authorizeAny(regularUser, "ADMIN", "MODERATOR")).isFalse();
        assertThat(authorizer.authorizeAny(adminUser, "SUPER_USER", "GUEST")).isFalse();
    }

    @Test
    @DisplayName("Should not authorize any role with null user")
    void shouldNotAuthorizeAnyRoleWithNullUser() {
        // When & Then
        assertThat(authorizer.authorizeAny(null, "USER", "ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Should not authorize any role with null roles array")
    void shouldNotAuthorizeAnyRoleWithNullRolesArray() {
        // When & Then
        assertThat(authorizer.authorizeAny(adminUser, (String[]) null)).isFalse();
    }

    @Test
    @DisplayName("Should not authorize any role with empty roles array")
    void shouldNotAuthorizeAnyRoleWithEmptyRolesArray() {
        // When & Then
        assertThat(authorizer.authorizeAny(adminUser)).isFalse();
    }

    @Test
    @DisplayName("Should authorize all roles when user has all of them")
    void shouldAuthorizeAllRolesWhenUserHasAllOfThem() {
        // When & Then
        assertThat(authorizer.authorizeAll(adminUser, "ADMIN", "USER")).isTrue();
        assertThat(authorizer.authorizeAll(moderatorUser, "MODERATOR", "USER")).isTrue();
    }

    @Test
    @DisplayName("Should not authorize all roles when user missing some")
    void shouldNotAuthorizeAllRolesWhenUserMissingSome() {
        // When & Then
        assertThat(authorizer.authorizeAll(regularUser, "USER", "ADMIN")).isFalse();
        assertThat(authorizer.authorizeAll(adminUser, "ADMIN", "SUPER_USER")).isFalse();
    }

    @Test
    @DisplayName("Should not authorize all roles with null user")
    void shouldNotAuthorizeAllRolesWithNullUser() {
        // When & Then
        assertThat(authorizer.authorizeAll(null, "USER", "ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Should not authorize all roles with null roles array")
    void shouldNotAuthorizeAllRolesWithNullRolesArray() {
        // When & Then
        assertThat(authorizer.authorizeAll(adminUser, (String[]) null)).isFalse();
    }

    @Test
    @DisplayName("Should not authorize all roles with empty roles array")
    void shouldNotAuthorizeAllRolesWithEmptyRolesArray() {
        // When & Then
        assertThat(authorizer.authorizeAll(adminUser)).isFalse();
    }

    @Test
    @DisplayName("Should identify admin user correctly")
    void shouldIdentifyAdminUserCorrectly() {
        // When & Then
        assertThat(authorizer.isAdmin(adminUser)).isTrue();
        assertThat(authorizer.isAdmin(regularUser)).isFalse();
        assertThat(authorizer.isAdmin(moderatorUser)).isFalse();
        assertThat(authorizer.isAdmin(userWithNoRoles)).isFalse();
    }

    @Test
    @DisplayName("Should not identify admin with null user")
    void shouldNotIdentifyAdminWithNullUser() {
        // When & Then
        assertThat(authorizer.isAdmin(null)).isFalse();
    }

    @Test
    @DisplayName("Should identify moderator or admin correctly")
    void shouldIdentifyModeratorOrAdminCorrectly() {
        // When & Then
        assertThat(authorizer.isModeratorOrAdmin(adminUser)).isTrue();
        assertThat(authorizer.isModeratorOrAdmin(moderatorUser)).isTrue();
        assertThat(authorizer.isModeratorOrAdmin(regularUser)).isFalse();
        assertThat(authorizer.isModeratorOrAdmin(userWithNoRoles)).isFalse();
    }

    @Test
    @DisplayName("Should not identify moderator or admin with null user")
    void shouldNotIdentifyModeratorOrAdminWithNullUser() {
        // When & Then
        assertThat(authorizer.isModeratorOrAdmin(null)).isFalse();
    }

    @Test
    @DisplayName("Should handle user with null roles")
    void shouldHandleUserWithNullRoles() {
        // Given
        GameUser userWithNullRoles = new GameUser("testuser", null);

        // When & Then
        assertThat(authorizer.authorize(userWithNullRoles, "USER")).isFalse();
        assertThat(authorizer.authorizeAny(userWithNullRoles, "USER", "ADMIN")).isFalse();
        assertThat(authorizer.authorizeAll(userWithNullRoles, "USER", "ADMIN")).isFalse();
        assertThat(authorizer.isAdmin(userWithNullRoles)).isFalse();
        assertThat(authorizer.isModeratorOrAdmin(userWithNullRoles)).isFalse();
    }
}
