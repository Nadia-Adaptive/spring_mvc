package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.organisation.OrganisationService;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRole;
import com.weareadaptive.auction.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static com.weareadaptive.auction.TestData.ORG_3;
import static com.weareadaptive.auction.TestData.PASSWORD;
import static com.weareadaptive.auction.TestData.USER1;

@Component
public class ControllerTestData {
    public static final String ADMIN_AUTH_TOKEN = "Bearer ADMIN:adminpassword";
    private final UserService userService;
    private final OrganisationService organisationService;
    private final Faker faker;
    private User user1;

    public ControllerTestData(UserService userService, OrganisationService organisationService) {
        this.userService = userService;
        this.organisationService = organisationService;

        faker = new Faker();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData() {
        user1 = createRandomUser();
        organisationService.addOrganisation(ORG_1);
        organisationService.addOrganisation(ORG_3);
        organisationService.addUser(USER1);

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

    public String getToken(User user) {
        return "Bearer " + user.getUsername() + ":" + PASSWORD;
    }
}
