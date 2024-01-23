package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.stream.Stream;

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

    private static Stream<String> invalidInput() {
        return Stream.of("""
                        { "username": "", "firstName": "test03", "lastName": "test03",
                        "password": "password", "organisation": "Organisation 1" }""",
                """
                        { "username": "test03", "firstName": "", "lastName": "test03",
                            "password": "password", "organisation": "Organisation 1"  }""",
                """
                        { "username": "test03", "firstName": "test03", "lastName": null,
                         "password": "password", "organisation": "Organisation 1" }""",
                """
                        {   "username": "test03", "firstName": "test03",
                            "lastName": "test03", "password": "",
                            "organisation": "Organisation 1" }""",
                """
                        { "username":"test_03", "firstName":"test03", "lastName":"test03",
                         "password":"password", "organisation":"Organisation 1"  }""");
    }

    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/user/";
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
                .get(String.valueOf(id)).then()
                .statusCode(200).assertThat()
                .body(
                        "id", equalTo(id), "username", equalTo("test01"),
                        "firstName", equalTo("test"), "lastName", equalTo("test"),
                        "organisation", equalTo("org1"), "accessStatus", equalTo("ALLOWED"), "admin", equalTo(false));
    }

    @Test
    public void getUser_UserDoesntExistAndRoleIsAdmin_ReturnsMessageAnd404() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when().get("-1").then()
                .statusCode(404);
    }

    @Test
    public void getUser_RoleIsUser_ReturnsMessageAnd403() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, testData.user1Token())
                .when().get("3").then()
                .statusCode(403);
    }

    @Test
    public void postUser_createUserWithValidInputs_ReturnsMessageAnd200() {
        final String userInput = """
                   {   "username": "test03", "firstName": "test03",
                   "lastName": "test03", "password": "invalid",
                   "organisation": "Organisation 1" }""";
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(userInput)
                .post()
                .then()
                .statusCode(200);
    }

    @ParameterizedTest()
    @MethodSource("invalidInput")
    public void postUser_createUserWithInvalidInputs_ReturnsMessageAnd400(final String input) {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .post()
                .then()
                .statusCode(400);
    }
}