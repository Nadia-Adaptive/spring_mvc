package com.weareadaptive.auction.security;

import static com.weareadaptive.auction.ControllerTestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityTest {
    @Autowired
    private ControllerTestData testData;
    @LocalServerPort
    private int port;
    private String uri;

    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone() {
        uri = "http://localhost:" + port;
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
                .body("message", equalTo(ErrorMessage.UNAUTHORIZED.getMessage()));
        //@formatter:on
    }

    @Test
    public void shouldBeAuthenticated() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
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
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
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
                .body("message", equalTo(ErrorMessage.FORBIDDEN.getMessage()));
        //@formatter:on
    }
}
