package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.organisation.OrganisationRepository;
import com.weareadaptive.auction.organisation.OrganisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.weareadaptive.auction.TestData.ADMIN;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static com.weareadaptive.auction.TestData.USER1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {
    private UserService userService;
    private OrganisationService organisationService;
    private final String password = "password";

    @BeforeEach
    public void initState() {
        final var userRepo = new UserRepository();
        userRepo.add(ADMIN);
        organisationService = new OrganisationService(new OrganisationRepository());
        userService = new UserService(userRepo, organisationService);
        userService.createUser(USER1.getUsername(), password, USER1.getFirstName(), USER1.getLastName(),
                USER1.getOrganisationName(), UserRole.USER);
        organisationService.addOrganisation(ORG_2);
    }

    @Test
    @DisplayName("getUser should return a user when passed a valid id")
    public void GetUser_PassedValidId_ReturnUser() {
        final var user = userService.getUser(0);
        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("getUser should throw a BusinessException when passed an invalid id")
    public void GetUser_PassedInvalidId_ReturnUser() {
        assertThrows(NotFoundException.class, () -> userService.getUser(-1));
    }

    @Test
    @DisplayName("validateUserCredentials should return a user when passed valid credentials")
    public void ValidateUserCredentials_PassedValidCredentials_ReturnNewUser() {
        final var user = userService.validateUserCredentials(ADMIN.getUsername(), "admin");
        assertEquals(ADMIN, user);
    }

    @Test
    @DisplayName("validateUserCredentials should return null when passed invalid credentials")
    public void ValidateUserCredentials_PassedInvalidCredentials_ReturnNull() {
        final var user = userService.validateUserCredentials(ADMIN.getUsername(), password);
        assertNull(user);
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void CreateUser_PassedValidInputs_DoesNotThrow() {
        assertDoesNotThrow(
                () -> userService.createUser("username", "password", "firstName", "lastName", "organisation",
                        UserRole.USER));
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void CreateUser_PassedValidInputs_AddsUserToOrganisation() {
        final var user = userService.createUser("username", "password", "firstName", "lastName", ORG_1,
                UserRole.USER);

        assertTrue(organisationService.get(ORG_1).users().contains(user));
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void CreateUser_PassedInvalidUsername_ThrowsBusinessException() {
        assertThrows(BusinessException.class,
                () -> userService.createUser("user_name", "password", "firstName", "lastName", "organisation",
                        UserRole.USER));
    }

    @Test()
    @DisplayName("update should return a user when passed valid input")
    public void UpdateUser_PassedValidInput_ReturnsUser() {
        final var user = userService.updateUser(1, "", "Hi", "test", ORG_2);
        assertEquals(USER1, user);
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUser_PassedInvalidUsername_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.updateUser(-1, "password", "firstName", "lastName", "organisation"));
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUserStatus_PassedValidStatus_ReturnsUser() {
        final var user = userService.updateUserStatus(1, AccessStatus.BLOCKED);
        assertEquals(USER1, user);
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUserStatus_PassedInvalidId_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.updateUserStatus(-1, AccessStatus.BLOCKED));
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
