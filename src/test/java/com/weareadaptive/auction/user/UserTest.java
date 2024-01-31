package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORGANISATION2;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private static Stream<Arguments> createUserArguments() {
        return Stream.of(
                Arguments.of("username",
                        (Executable) () -> new User(1, null, "pp", "first", "last", ORGANISATION1, UserRole.USER)),
                Arguments.of("firstName",
                        (Executable) () -> new User(1, "username", "pp", null, "last", ORGANISATION1, UserRole.USER)),
                Arguments.of("lastName",
                        (Executable) () -> new User(1, "username", "pp", "first", null, ORGANISATION1, UserRole.USER)),
                Arguments.of("organisation",
                        (Executable) () -> new User(1, "username", "pp", "first", "last", null, UserRole.USER)),
                Arguments.of("password",
                        (Executable) () -> new User(1, "username", null, "first", "last", ORGANISATION1, UserRole.USER))
        );
    }

    @ParameterizedTest(name = "Create user should throw when {0} is null")
    @MethodSource("createUserArguments")
    public void createUserShouldThrowWhenNullProperty(final String propertyName, final Executable userExecutable) {
        var exception = assertThrows(BusinessException.class, userExecutable);

        assertTrue(exception.getMessage().contains(propertyName));
    }

    @Test
    @DisplayName("ValidatePassword should return false when the password is not valid")
    public void shouldReturnFalseWhenThePasswordIsNotValid() {
        final var user = new User(1, "test", "password", "John", "Doe", ORGANISATION1, UserRole.USER);
        final var result = user.validatePassword("bad");

        assertFalse(result);
    }

    @Test
    @DisplayName("ValidatePassword should return true when the password is valid")
    public void shouldReturnTrueWhenThePasswordIsValid() {
        final var user = new User(1, "test", "password", "John", "Doe", ORGANISATION1, UserRole.USER);

        final var result = user.validatePassword("password");

        assertTrue(result);
    }

    @Test
    @DisplayName("Update_NotEmptyUserFields_UserFieldsUpdated")
    public void updateNonEmptyFields() {
        final var user = new User(1, "test", "password", "John", "Doe", ORGANISATION1, UserRole.USER);
        final var testName = "Test user01";

        user.update("", testName, "", ORGANISATION2);

        assertEquals(testName, user.getFirstName());
        assertEquals(ORGANISATION2.organisationName(), user.getOrganisationName());
    }

    @Test
    @DisplayName("Update_EmptyUserFields_UserFieldsNotUpdated")
    public void updateIgnoresEmptyFields() {
        final var user = new User(1, "test", "password", "John", "Doe", ORGANISATION1, UserRole.USER);

        user.update("", "", "", null);

        assertEquals("test", user.getUsername());
    }

    @Test
    @DisplayName("a new user's default access status should be allowed")
    public void userDefaultStatusIsAllowed() {
        final var user = new User(1, "test", "password", "John", "Doe", ORGANISATION1, UserRole.USER);
        Assertions.assertEquals(AccessStatus.ALLOWED, user.getAccessStatus());
    }
}
