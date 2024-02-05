package com.weareadaptive.auction.user;

import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.auction.AuctionRepository;
import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.organisation.OrganisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.AUCTION1;
import static com.weareadaptive.auction.TestData.AUCTION2;
import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORGANISATION2;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static com.weareadaptive.auction.TestData.USER1;
import static com.weareadaptive.auction.TestData.USER2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserService userService;
    private OrganisationRepository organisationRepo;
    private final String password = "password";

    @BeforeEach
    public void initState() {
        final var userRepo = mock(UserRepository.class);
        final var auctionRepo = mock(AuctionRepository.class);
        organisationRepo = mock(OrganisationRepository.class);

        when(userRepo.getUserByUsername(USER1.getUsername())).thenReturn(USER1);
        when(userRepo.getUserById(0)).thenReturn(USER1);
        when(userRepo.getUserById(1)).thenReturn(USER2);

        when(organisationRepo.getOrganisationByName(ORG_1)).thenReturn(ORGANISATION1);

        when(auctionRepo.getUserAuctions(0)).thenReturn((Stream.of(AUCTION1, AUCTION2)));
        when(auctionRepo.getUserAuctions(1)).thenReturn(Stream.<Auction>builder().build());

        userService = new UserService(userRepo, organisationRepo, auctionRepo);
    }

    @Test
    @DisplayName("getUser should return a user when passed a valid id")
    public void getUser() {
        final var user = userService.getUser(0);
        assertEquals(USER1, user);
    }

    @Test
    @DisplayName("getUser should throw a BusinessException when passed an invalid id")
    public void getUserPassedInvalidId() {
        assertThrows(NotFoundException.class, () -> userService.getUser(-1));
    }

    @Test()
    @DisplayName("createUser should create a user when passed valid parameters")
    public void createUser() {
        final var user = userService.createUser("username", "password", "firstName", "lastName", ORG_1,
                UserRole.USER);

        assertEquals(ORGANISATION1, organisationRepo.getOrganisationByName(user.getOrganisationName()));
    }

    @Test()
    @DisplayName("createUser should throws a business exception when passed invalid username")
    public void createUserPassedInvalidUsername() {
        assertThrows(BusinessException.class,
                () -> userService.createUser("user_name", "password", "firstName", "lastName", "organisation",
                        UserRole.USER));
    }

    @Test()
    @DisplayName("update should return a user when passed valid input")
    public void updateUser() {
        when(organisationRepo.getOrganisationByName(ORG_2)).thenReturn(ORGANISATION2);
        final var user = userService.updateUser(0, "", "Hi", "test", ORG_2);
        assertEquals(ORGANISATION2, organisationRepo.getOrganisationByName(user.getOrganisationName()));
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void updateUserPassedInvalidUsername() {
        assertThrows(NotFoundException.class,
                () -> userService.updateUser(-1, "password", "firstName", "lastName", "organisation"));
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void updateUserStatus() {
        final var user = userService.updateUserStatus(0, AccessStatus.BLOCKED);
        assertEquals(AccessStatus.BLOCKED, user.getAccessStatus());
    }

    @Test()
    @DisplayName("update should throws a business exception when passed invalid username")
    public void updateUserStatusPassedInvalidId() {
        assertThrows(NotFoundException.class,
                () -> userService.updateUserStatus(-1, AccessStatus.BLOCKED));
    }

    @Test()
    @DisplayName("getUserAuctions should return all the user's auctions")
    public void getUserAuction() {
        assertEquals(2,  userService.getUserAuctions(0).size());
    }

    @Test()
    @DisplayName("getUserAuctions should return an empty list if  the user has no auctions")
    public void getUserAuctionNoAuctions() {
        assertTrue(userService.getUserAuctions(1).isEmpty());
    }

    @Test()
    @DisplayName("getUserAuctions should throw if the user doesn't exist")
    public void getUserAuctionUserDoesntExist() {
        assertThrows(NotFoundException.class, () -> userService.getUserAuctions(-1));
    }
}
