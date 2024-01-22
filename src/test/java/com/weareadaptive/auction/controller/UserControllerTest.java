package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    UserState state;

    @Autowired
    TestData testData;

    @LocalServerPort
    private int port;
    private String uri;

    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port;
    }

    @Test
    public void getUser_UserExistsAndRoleIsAdmin_ReturnsUserAnd200() {
        final var id = state.nextId();
        final var user = new User(id, "test01", "password", "test", "test", "org1");
        state.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .get("/user/"+id).then()
                .statusCode(200).assertThat()
                .body(
                        "id", equalTo(id), "username", equalTo("test01"),
                        "firstName", equalTo("test"), "lastName", equalTo("test"),
                        "organisation", equalTo("org1"), "status", equalTo("PENDING"), "admin", equalTo(false));
    }

    @Test
    public void getUser_UserDoesntExistAndRoleIsAdmin_ReturnsMessageAnd404() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when().get(uri + "/user/-1").then()
                .statusCode(404).assertThat()
                .body("message", equalTo("User not found."));
    }

    @Test
    public void getUser_RoleIsUser_ReturnsMessageAnd403() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, testData.user1Token())
                .when().get(uri + "/user/3").then()
                .statusCode(403);
    }
}