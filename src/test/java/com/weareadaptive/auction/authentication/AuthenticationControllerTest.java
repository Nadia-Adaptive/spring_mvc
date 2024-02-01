package com.weareadaptive.auction.authentication;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.response.ResponseStatus;
import com.weareadaptive.auction.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {
    ControllerTestData testData;

    @LocalServerPort
    private int port;
    private String uri;

    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/auth/";
    }

    @Test
    @DisplayName("PostLogin_ValidCredentials_ReturnsToken")
    public void postLogin() {
        // @formatter:off
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
                .body("data.token", containsString("eyJhbGciOiJIUzI1NiJ9"),
                        "data.role", containsString(UserRole.ADMIN.name()),
                        "data.username", containsString("ADMIN"));
        // @formatter:on

    }

    @Test
    @DisplayName("PostLogin_InvalidCredentials_ReturnsMessage")
    public void postLoginWithInvalidCredentials() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
        .when()
                .body("""
                        {   
                         "username": "ADMIN",
                         "password": "pass"
                        }""")
                .post("login")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(ResponseStatus.BAD_CREDENTIALS.getMessage()));
        //@formatter:on
    }
}
