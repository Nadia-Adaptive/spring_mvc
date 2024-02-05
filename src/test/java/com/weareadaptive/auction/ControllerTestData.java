package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.auction.AuctionService;
import com.weareadaptive.auction.authentication.AuthenticationService;
import com.weareadaptive.auction.organisation.OrganisationService;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRole;
import com.weareadaptive.auction.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_3;
import static com.weareadaptive.auction.TestData.PASSWORD;
import static com.weareadaptive.auction.security.AuthenticationFilter.BEARER;

@Component
public class ControllerTestData {
    public static String adminAuthToken;
    private final UserService userService;
    private final AuctionService auctionService;
    private final OrganisationService organisationService;

    private final AuthenticationService authenticationService;
    private final Faker faker;
    private User admin;
    private User user1;

    public ControllerTestData(final UserService userService, final AuctionService auctionService,
                              final OrganisationService organisationService,
                              final AuthenticationService authenticationService) {
        this.userService = userService;
        this.auctionService = auctionService;
        this.organisationService = organisationService;
        this.authenticationService = authenticationService;

        userService.createUser("admin", "admin", "admin", "admin", "ADMIN", UserRole.ADMIN);

        adminAuthToken = BEARER + authenticationService.generateJWTToken("admin");
        auctionService.createAuction(1, "TEST", 1.0, 10);

        faker = new Faker();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData() {
        user1 = createRandomUser();

        organisationService.addOrganisation(ORG_1);
        organisationService.addOrganisation(ORG_3);
    }

    public String user1Token() {
        return getToken(user1);
    }

    public User createRandomUser() {
        var name = faker.name();
        var user = userService.createUser(
                "user01",
                PASSWORD,
                name.firstName(),
                name.lastName(),
                ORG_1,
                UserRole.USER
        );
        return user;
    }

    public String getToken(final User user) {
        return BEARER + authenticationService.generateJWTToken(user.getUsername());
    }
}
