package com.weareadaptive.auction.authentication;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.ResponseStatus;
import com.weareadaptive.auction.organisation.OrganisationService;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
    @Autowired
    UserRepository repository;

    @Autowired
    OrganisationService organisationService;

    @Autowired
    ControllerTestData testData;

    @LocalServerPort
    private int port;
    private String uri;

    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/auth/";
    }

    @Test
    public void PostLogin_ValidCredentials_ReturnsToken(){
        //@formatter: off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
        .when()
                .body("""
                        {   "username": "ADMIN",
                            "password": "adminpassword"
                            }""")
                .post("login")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", equalTo("ADMIN:adminpassword"));
        //@formatter: on

    }

    @Test
    public void PostLogin_InvalidCredentials_ReturnsMessage(){
        //@formatter: off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
        .when()
                .body("""
                        {   "username": "ADMIN",
                            "password": "pass"
                            }""")
                .post("login")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(ResponseStatus.BAD_CREDENTIALS.getMessage()));
        //@formatter: on

    }
}