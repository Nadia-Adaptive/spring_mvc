package com.weareadaptive.auction;

import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.organisation.Organisation;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TestData {
    public static final String ORG_1 = "Org 1";
    public static final String ORG_2 = "Org 2";
    public static final String ORG_3 = "Org 3";
    public static final Organisation ORGANISATION1 = new Organisation(10, ORG_1, new ArrayList<>());
    public static final Organisation ORGANISATION2 = new Organisation(11, ORG_2, new ArrayList<>() {{
        add(USER3);
    }});

    public static final Organisation ORGANISATION3 = new Organisation(12, ORG_3, new ArrayList<>() {{
        add(USER3);
    }});

    public static final Organisation ADMIN_ORGANISATION = new Organisation(13, "ADMIN", new ArrayList<>() {{
        add(ADMIN);
    }});

    public static final User ADMIN =
            new User(0, "admin", "admin", "admin", "admin", ADMIN_ORGANISATION, UserRole.ADMIN);
    public static final User USER1 = new User(1, "testuser1", "password", "john", "doe", ORGANISATION1, UserRole.USER);
    public static final User USER2 =
            new User(2, "testuser2", "password", "john", "smith", ORGANISATION1, UserRole.USER);
    public static final User USER3 = new User(3, "testuser3", "password", "jane", "doe", ORGANISATION2, UserRole.USER);
    public static final User USER4 =
            new User(4, "testuser4", "password", "naomie", "legault", ORGANISATION2, UserRole.USER);

    public static final Auction AUCTION1 = new Auction(0, USER4, "TEST", 1.0, 10);
    public static final Auction AUCTION2 = new Auction(1, USER3, "TEST2", 2.0, 11);
    public static final Auction AUCTION3 = new Auction(2, USER1, "TEST3", 3.0, 12);
    public static final String PASSWORD = "mypassword";
}
