package com.gamingroom.gameauth.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Unit tests for GameUser class.
 * 
 * @author GameAuth Team
 * @version 1.0
 */
@DisplayName("GameUser Tests")
class GameUserTest {

    private Set<String> testRoles;
    private GameUser testUser;

    @BeforeEach
    void setUp() {
        testRoles = new HashSet<>();
        testRoles.add("USER");
        testRoles.add("ADMIN");
        testUser = new GameUser("testuser", testRoles);
    }

    @Test
    @DisplayName("Should create user with valid name and roles")
    void shouldCreateUserWithValidNameAndRoles() {
        // Given
        String name = "testuser";
        Set<String> roles = Set.of("USER", "ADMIN");

        // When
        GameUser user = new GameUser(name, roles);

        // Then
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getRoles()).isEqualTo(roles);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getId()).isNotEmpty();
    }

    @Test
    @DisplayName("Should create user with null roles")
    void shouldCreateUserWithNullRoles() {
        // Given
        String name = "testuser";

        // When
        GameUser user = new GameUser(name, null);

        // Then
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getRoles()).isNull();
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should create user with single parameter constructor")
    void shouldCreateUserWithSingleParameterConstructor() {
        // Given
        String name = "testuser";

        // When
        GameUser user = new GameUser(name);

        // Then
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getRoles()).isNull();
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception for null name")
    void shouldThrowExceptionForNullName() {
        // When & Then
        assertThatThrownBy(() -> new GameUser(null, testRoles))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception for empty name")
    void shouldThrowExceptionForEmptyName() {
        // When & Then
        assertThatThrownBy(() -> new GameUser("", testRoles))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception for whitespace-only name")
    void shouldThrowExceptionForWhitespaceOnlyName() {
        // When & Then
        assertThatThrownBy(() -> new GameUser("   ", testRoles))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username cannot be null or empty");
    }

    @Test
    @DisplayName("Should trim whitespace from name")
    void shouldTrimWhitespaceFromName() {
        // Given
        String nameWithWhitespace = "  testuser  ";

        // When
        GameUser user = new GameUser(nameWithWhitespace, testRoles);

        // Then
        assertThat(user.getName()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should return true when user has specific role")
    void shouldReturnTrueWhenUserHasSpecificRole() {
        // When & Then
        assertThat(testUser.hasRole("USER")).isTrue();
        assertThat(testUser.hasRole("ADMIN")).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not have specific role")
    void shouldReturnFalseWhenUserDoesNotHaveSpecificRole() {
        // When & Then
        assertThat(testUser.hasRole("MODERATOR")).isFalse();
        assertThat(testUser.hasRole("SUPER_USER")).isFalse();
    }

    @Test
    @DisplayName("Should return false when checking role on user with null roles")
    void shouldReturnFalseWhenCheckingRoleOnUserWithNullRoles() {
        // Given
        GameUser userWithNullRoles = new GameUser("testuser", null);

        // When & Then
        assertThat(userWithNullRoles.hasRole("USER")).isFalse();
    }

    @Test
    @DisplayName("Should return true when user has any of the specified roles")
    void shouldReturnTrueWhenUserHasAnyOfSpecifiedRoles() {
        // When & Then
        assertThat(testUser.hasAnyRole("USER", "MODERATOR")).isTrue();
        assertThat(testUser.hasAnyRole("ADMIN", "SUPER_USER")).isTrue();
    }

    @Test
    @DisplayName("Should return false when user has none of the specified roles")
    void shouldReturnFalseWhenUserHasNoneOfSpecifiedRoles() {
        // When & Then
        assertThat(testUser.hasAnyRole("MODERATOR", "SUPER_USER")).isFalse();
    }

    @Test
    @DisplayName("Should return false when checking any role on user with null roles")
    void shouldReturnFalseWhenCheckingAnyRoleOnUserWithNullRoles() {
        // Given
        GameUser userWithNullRoles = new GameUser("testuser", null);

        // When & Then
        assertThat(userWithNullRoles.hasAnyRole("USER", "ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Should return false when checking any role with null array")
    void shouldReturnFalseWhenCheckingAnyRoleWithNullArray() {
        // When & Then
        assertThat(testUser.hasAnyRole((String[]) null)).isFalse();
    }

    @Test
    @DisplayName("Should generate unique IDs for different users")
    void shouldGenerateUniqueIdsForDifferentUsers() {
        // Given
        GameUser user1 = new GameUser("user1", testRoles);
        GameUser user2 = new GameUser("user2", testRoles);

        // When & Then
        assertThat(user1.getId()).isNotEqualTo(user2.getId());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        // Given
        GameUser user1 = new GameUser("testuser", testRoles);
        GameUser user2 = new GameUser("testuser", testRoles);
        GameUser user3 = new GameUser("differentuser", testRoles);

        // When & Then
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1).isEqualTo(user1);
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo("not a user");
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        // Given
        GameUser user1 = new GameUser("testuser", testRoles);
        GameUser user2 = new GameUser("testuser", testRoles);

        // When & Then
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Should have meaningful toString representation")
    void shouldHaveMeaningfulToStringRepresentation() {
        // When
        String toString = testUser.toString();

        // Then
        assertThat(toString).contains("testuser");
        assertThat(toString).contains("USER");
        assertThat(toString).contains("ADMIN");
        assertThat(toString).contains("GameUser");
    }
}
