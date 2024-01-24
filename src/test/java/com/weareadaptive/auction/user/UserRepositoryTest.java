package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.ADMIN;
import static com.weareadaptive.auction.TestData.USER1;
import static com.weareadaptive.auction.TestData.USER2;
import static com.weareadaptive.auction.TestData.USER3;
import static com.weareadaptive.auction.TestData.USER4;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("findUserByUsername should return a user when passed valid credentials")
    public void GetUserByUsername_PassedValidUsername_ReturnNewUser() {
        final var user = state.getUserByUsername(ADMIN.getUsername());
        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("findUserByUsername should return null when passed invalid credentials")
    public void GetUserByUsername_PassedInvalidUsername_ReturnNull() {
        final var user = state.getUserByUsername("notValid");
        assertEquals(null, user);
    }

    @Test
    @DisplayName("add should not throw when passed a valid user object")
    public void CreateUser_PassedValidCredentials_ReturnNull() {
        assertDoesNotThrow(() -> state.add(USER1));
    }

    @Test
    @DisplayName("add should throw when passed a duplicate user object")
    public void CreateUser_PassedInvalidCredentials_ReturnNull() {
        state.add(USER2);
        assertThrows(BusinessException.class, () -> state.add(USER2));
    }

//    @Test
//    @DisplayName("containsUser should return true when user is present in state")
//    public void shouldReturnTrueWhenUserIsInState() {
//        assertTrue(state.containsUser("admin"));
//    }
//
//    @Test
//    @DisplayName("containsUser should return false when user is not present in state")
//    public void shouldReturnFalseWhenUserIsNotInState() {
//        assertFalse(state.containsUser("testuser7"));
//    }
}
