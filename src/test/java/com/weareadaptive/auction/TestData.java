package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.model.Auction;
import com.weareadaptive.auction.model.Organisation;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRole;
import com.weareadaptive.auction.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TestData {
    public static final String ORG_1 = "Org 1";
    public static final String ORG_2 = "Org 2";
    public static final String AAPL = "AAPL";
    public static final String EBAY = "EBAY";
    public static final String FB = "FB";

    public static final User ADMIN = new User(0, "admin", "admin", "admin", "admin", "admin", UserRole.ADMIN);
    public static final User USER1 = new User(1, "testuser1", "password", "john", "doe", ORG_1, UserRole.USER);
    public static final User USER2 = new User(2, "testuser2", "password", "john", "smith", ORG_1, UserRole.USER);
    public static final User USER3 = new User(3, "testuser3", "password", "jane", "doe", ORG_2, UserRole.USER);
    public static final User USER4 = new User(4, "testuser4", "password", "naomie", "legault", ORG_2, UserRole.USER);

    public static final Organisation ORGANISATION1 = new Organisation(ORG_1, Arrays.asList(USER1));
    public static final Organisation ORGANISATION2 = new Organisation(ORG_1, Arrays.asList(USER3));
    public static final Auction AUCTION1 = new Auction(0, USER4, "TEST", 1.0, 10);
    public static final Auction AUCTION2 = new Auction(1, USER3, "TEST2", 2.0, 11);
    public static final Auction AUCTION3 = new Auction(2, USER1, "TEST3", 3.0, 12);
    public static final String PASSWORD = "mypassword";
    public static final String ADMIN_AUTH_TOKEN = "Bearer ADMIN:adminpassword";

    private final UserService userService;
    private final Faker faker;
    private User user1;

    public TestData(UserService userService) {
        this.userService = userService;
        faker = new Faker();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData() {
        user1 = createRandomUser();
    }

    public String user1Token() {
        return getToken(user1);
    }

    public User createRandomUser() {
        var name = faker.name();
        System.out.println(name.username());
        var user = userService.createUser(
                "user01",
                PASSWORD,
                name.firstName(),
                name.lastName(),
                faker.company().name(),
                UserRole.USER
        );
        return user;
    }

    public String getToken(User user) {
        return "Bearer " + user.getUsername() + ":" + PASSWORD;
    }


}
