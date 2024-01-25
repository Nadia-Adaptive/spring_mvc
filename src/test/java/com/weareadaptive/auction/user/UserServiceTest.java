package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.AccessStatus;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.user.UserRepository;
import com.weareadaptive.auction.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Executable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.ADMIN;
import static com.weareadaptive.auction.TestData.USER1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    private UserService service;
    private final String password = "password";

    @BeforeEach
    public void initState() {
        final var userRepo = new UserRepository();
        userRepo.add(ADMIN);
        service = new UserService(userRepo);
        service.createUser(USER1.getUsername(), password, USER1.getFirstName(), USER1.getLastName(),
                USER1.getOrganisation(), UserRole.USER);
    }

    @Test
    @DisplayName("getUser should return a user when passed a valid id")
    public void GetUser_PassedValidId_ReturnUser() {
        final var user = service.getUser(0);
        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("getUser should throw a BusinessException when passed an invalid id")
    public void GetUser_PassedInvalidId_ReturnUser() {
        assertThrows(NotFoundException.class, () -> service.getUser(-1));
    }

    @Test
    @DisplayName("validateUserCredentials should return a user when passed valid credentials")
    public void ValidateUserCredentials_PassedValidCredentials_ReturnNewUser() {
        final var user = service.validateUserCredentials(ADMIN.getUsername(), "admin");
        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("validateUserCredentials should return null when passed invalid credentials")
    public void ValidateUserCredentials_PassedInvalidCredentials_ReturnNull() {
        final var user = service.validateUserCredentials(ADMIN.getUsername(), password);
        assertEquals(null, user);
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void CreateUser_PassedInvalidUsername_ThrowsBusinessException() {
        assertThrows(BusinessException.class,
                () -> service.createUser("user_name", "password", "firstName", "lastName", "organisation",
                        UserRole.USER));
    }

    @Test()
    @DisplayName("update should return a user when passed valid input")
    public void UpdateUser_PassedValidInput_ReturnsUser() {
        final var user = service.updateUser(1, "", "Hi", "test", "organisation");
        assertEquals(USER1, user);
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUser_PassedInvalidUsername_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> service.updateUser(-1, "password", "firstName", "lastName", "organisation"));
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUserStatus_PassedValidStatus_ReturnsUser() {
        final var user = service.updateUserStatus(1, AccessStatus.BLOCKED);
        assertEquals(USER1, user);
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUserStatus_PassedInvalidId_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> service.updateUserStatus(-1, AccessStatus.BLOCKED));
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
