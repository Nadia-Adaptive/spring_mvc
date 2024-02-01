package com.weareadaptive.auction.security;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.response.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.weareadaptive.auction.ControllerTestData.adminAuthToken;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTest {
    @Autowired
    private ControllerTestData testData;
    @LocalServerPort
    private int port;
    private String uri;

    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone() {
        uri = "http://localhost:" + port + "/api/v1";
    }

    @Test
    public void shouldBeUnauthorizedWhenNotAuthenticated() {
        //@formatter:off
        given()
                .baseUri(uri)
         .when()
                .get("/test")
         .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("message", equalTo(ResponseStatus.UNAUTHORIZED.getMessage()));
        //@formatter:on
    }

    @Test
    public void shouldBeAuthenticated() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .get("/test")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("houra"));
        //@formatter:on
    }

    @Test
    public void shouldBeAnAdmin() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .get("/test/adminOnly")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("super"));
        //@formatter:on
    }

    @Test
    public void shouldReturnForbiddenWhenNotAnAdmin() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, testData.user1Token())
                .when()
                .get("/test/adminOnly")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo(ResponseStatus.FORBIDDEN.getMessage()));
        //@formatter:on
    }

    @Test
    public void shouldReturnUnauthorizedWhenProvidedInvalidToken() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, "invalid")
                .when()
                .get("/test/adminOnly")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("message", equalTo(ResponseStatus.UNAUTHORIZED.getMessage()));
        //@formatter:on
    }
}
