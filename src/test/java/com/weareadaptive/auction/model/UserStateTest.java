package com.weareadaptive.auction.model;

import com.weareadaptive.auction.model.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.ADMIN;
import static com.weareadaptive.auction.TestData.USER1;
import static com.weareadaptive.auction.TestData.USER2;
import static com.weareadaptive.auction.TestData.USER3;
import static com.weareadaptive.auction.TestData.USER4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserStateTest {
    private UserState state;

    @BeforeEach
    public void initState() {
        state = new UserState();
        Stream.of(
                ADMIN,
                USER1,
                USER2,
                USER3,
                USER4
        ).forEach(u -> state.add(u));
    }

    @Test
    @DisplayName("findUserByUsername should return a user when passed valid credentials")
    public void shouldGetUserWhenPassedValidCredentials() {
        final var user = state.getUserByUsername(ADMIN.getUsername());

        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("findUserByUsername should return a user when passed valid credentials")
    public void shouldFindUserWhenPassedValidCredentials() {
        final var user = state.validateUsernamePassword(ADMIN.getUsername(), "admin");

        assertEquals(ADMIN, user.get());
    }

    @Test
    @DisplayName("findUserByUsername should return null when passed invalid credentials")
    public void shouldReturnNullWhenPassedInvalidCredentials() {
        final var user = state.validateUsernamePassword(ADMIN.getUsername(), "password");

        assertTrue(user.isEmpty());
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
