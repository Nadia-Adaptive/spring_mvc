package com.weareadaptive.auction.user;

import com.weareadaptive.auction.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.weareadaptive.auction.TestData.ADMIN;
import static com.weareadaptive.auction.TestData.USER1;
import static com.weareadaptive.auction.TestData.USER2;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRepositoryTest {
    private UserRepository state;

    @BeforeEach
    public void initState() {
        state = new UserRepository();
        state.add(ADMIN);
    }

    @Test
    @DisplayName("findUserByUsername should return a user when passed valid credentials")
    public void shouldGetUserWhenPassedValidCredentials() {
        final var user = state.getUserByUsername(ADMIN.getUsername());
        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("findUserByUsername should return null when passed invalid credentials")
    public void getUserByUsernamePassedInvalidUsername() {
        final var user = state.getUserByUsername("notValid");
        assertEquals(null, user);
    }

    @Test
    @DisplayName("add should not throw when passed a valid user object")
    public void createUser() {
        assertDoesNotThrow(() -> state.add(USER1));
    }

    @Test
    @DisplayName("add should throw when passed a duplicate user object")
    public void createUserPassedInvalidCredentials() {
        state.add(USER2);
        assertThrows(BusinessException.class, () -> state.add(USER2));
    }

    @Test
    @DisplayName("getUserById returns a user")
    public void getUserById() {
        assertEquals(ADMIN, state.getUserById(0));
    }

    @Test
    @DisplayName("getUserById returns null when passed an invalid id")
    public void getUserByInvalidId() {
        assertEquals(null, state.getUserById(-1));
    }
}
