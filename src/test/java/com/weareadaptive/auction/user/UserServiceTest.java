package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.organisation.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORGANISATION2;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static com.weareadaptive.auction.TestData.USER1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserService userService;
    private OrganisationRepository organisationRepo;
    private final String password = "password";

    @BeforeEach
    public void initState() {
        final var userRepo = mock(UserRepository.class);
        organisationRepo = mock(OrganisationRepository.class);

        when(userRepo.getUserByUsername(USER1.getUsername())).thenReturn(USER1);
        when(userRepo.getUserById(0)).thenReturn(USER1);
        when(organisationRepo.getOrganisationByName(ORG_1)).thenReturn(ORGANISATION1);
        userService = new UserService(userRepo, organisationRepo);
    }

    @Test
    @DisplayName("getUser should return a user when passed a valid id")
    public void GetUser_PassedValidId_ReturnUser() {
        final var user = userService.getUser(0);
        assertEquals(USER1, user);
    }

    @Test
    @DisplayName("getUser should throw a BusinessException when passed an invalid id")
    public void GetUser_PassedInvalidId_ReturnUser() {
        assertThrows(NotFoundException.class, () -> userService.getUser(-1));
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void CreateUser_PassedValidInputs_DoesNotThrow() {
        assertDoesNotThrow(
                () -> userService.createUser("username", "password", "firstName", "lastName", ORG_1,
                        UserRole.USER));
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void CreateUser_PassedValidInputs_AddsUserToOrganisation() {
        final var user = userService.createUser("username", "password", "firstName", "lastName", ORG_1,
                UserRole.USER);

        assertEquals(ORGANISATION1, organisationRepo.getOrganisationByName(user.getOrganisationName()));
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
        when(organisationRepo.getOrganisationByName(ORG_2)).thenReturn(ORGANISATION2);
        final var user = userService.updateUser(0, "", "Hi", "test", ORG_2);
        assertEquals(ORGANISATION2, organisationRepo.getOrganisationByName(user.getOrganisationName()));
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
        final var user = userService.updateUserStatus(0, AccessStatus.BLOCKED);
        assertEquals(AccessStatus.BLOCKED, user.getAccessStatus());
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void UpdateUserStatus_PassedInvalidId_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.updateUserStatus(-1, AccessStatus.BLOCKED));
    }
}
