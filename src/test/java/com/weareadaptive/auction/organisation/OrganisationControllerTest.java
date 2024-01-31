package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.weareadaptive.auction.ControllerTestData.ADMIN_AUTH_TOKEN;
import static com.weareadaptive.auction.TestData.ADMIN_ORGANISATION;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_3;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrganisationControllerTest {
    @Autowired
    OrganisationService service;

    @Autowired
    TestData testData;

    @LocalServerPort
    private int port;
    private String uri;


    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/api/v1/organisations/";
    }

    @Test
    void GetAllOrganisations_ReturnsAllOrganisations() {
        given().
                baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItems(3, 2, 4),
                        "organisationName", hasItems("ADMIN", ORG_1, ORG_3));

    }

    @Test
    void GetOrganisation_ValidId_ReturnsOrganisation() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .get("1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1),
                        "organisationName", equalTo(ADMIN_ORGANISATION.organisationName()),
                        "users", emptyCollectionOf(User.class)
                );
    }
}
