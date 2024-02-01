package com.weareadaptive.auction.user;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.response.ResponseStatus;
import com.weareadaptive.auction.organisation.OrganisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static com.weareadaptive.auction.ControllerTestData.adminAuthToken;
import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    UserRepository repository;

    @Autowired
    OrganisationService organisationService;

    @Autowired
    ControllerTestData testData;

    @LocalServerPort
    private int port;
    private String uri;

    private static String username = "test";
    private static String password = "testPassword";

    private static String generateInput(final String username, final String firstName, final String lastName,
                                        final String password, final String organisationName, final String userRole) {
        return """
                { "username": "%s", "firstName": "%s", "lastName": "%s",
                "password": "%s", "organisationName": "%s", "userRole": "%s" }""".formatted(username, firstName,
                lastName, password, organisationName, userRole);
    }

    private static Stream<String> invalidInput() {
        return Stream.of(
                generateInput(username, "", username, password, ORG_1, UserRole.USER.name()),
                generateInput(username, username, "", password, ORG_1, UserRole.USER.name()),
                generateInput(username, username, username, "", ORG_1, UserRole.USER.name()),
                generateInput(username, username, username, password, "", UserRole.USER.name()),
                generateInput(username, username, username, password, ORG_1, "INVALID"),
                generateInput(username, username, username, password, ORG_1, UserRole.ADMIN.name()),
                generateInput(username, username, username, password, "ADMIN", UserRole.USER.name()));
    }

    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/api/v1/users/";
    }

    @Test
    @DisplayName("GetUser_UserExistsAndRoleIsAdmin_ReturnsUserAnd200")
    public void getUser() {
        final var id = repository.nextId();
        final var user = new User(id, "test01", "password", "test", "test", ORGANISATION1, UserRole.USER);
        repository.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .get(String.valueOf(id)).then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "data.id", equalTo(id), "data.username", equalTo("test01"),
                        "data.firstName", equalTo("test"), "data.lastName", equalTo("test"),
                        "data.organisationName", equalTo(ORG_1), "data.accessStatus", equalTo("ALLOWED"), "data.userRole",
                        equalTo("USER"));
    }

    @Test
    @DisplayName("GetUser_UserDoesntExistAndRoleIsAdmin_ReturnsMessageAnd404")
    public void getNonExistentUser() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when().get("-1").then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ResponseStatus.NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("PostUser_CreateUserWithValidInputs_ReturnsMessageAnd201")
    public void postUser() {
        final String userInput = generateInput("username", username, username, password, ORG_1, UserRole.USER.name());
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .header("Content-Type", "application/json")
                .when()
                .body(userInput)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", equalTo(ResponseStatus.CREATED.getMessage()));
    }

    @Test
    @DisplayName("PostUser_CreateAdminWithValidInputs_ReturnsMessageAnd201")
    public void postAdmin() {
        final String userInput = generateInput(username, username, username, password, "ADMIN", UserRole.ADMIN.name());

        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .header("Content-Type", "application/json")
                .body(userInput)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", equalTo(ResponseStatus.CREATED.getMessage()));
    }

    @ParameterizedTest()
    @DisplayName("PostUser_createUserWithInvalidInputs_ReturnsMessageAnd400")
    @MethodSource("invalidInput")
    public void postUserHandlesInvalidInputs(final String input) {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message",
                        equalTo(ResponseStatus.BAD_REQUEST.getMessage()));
    }

    @Test()
    @DisplayName("PutUser_UpdateUserWithValidInputs_ReturnsMessageAnd200")
    public void putUser() {
        final String input = generateInput("", "", "", "", ORG_2, "");
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()));
    }

    @Test()
    @DisplayName("PutUser_AttemptUpdateNonExistentUser_ReturnsMessageAnd404")
    public void putNonExistentUser() {
        final String input = generateInput(username, username, username, password, ORG_1, UserRole.USER.name());

        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("-1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ResponseStatus.NOT_FOUND.getMessage()));
    }

    @Test()
    @DisplayName("PutUserStatus_UpdateUserWithValidAccessStatus_ReturnsMessageAnd200")
    public void putUserStatus() {
        final String input = "{\"accessStatus\": \"BLOCKED\"}";
        final var user = new User(30, "test02", "password", "test", "test", ORGANISATION1, UserRole.USER);
        repository.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("30/status")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()));
        assertEquals(AccessStatus.BLOCKED, user.getAccessStatus());
    }

    @Test()
    @DisplayName("PutUserStatus_UpdateUserWithValidAccessStatus_ReturnsMessageAnd400")
    public void putUserStatusWithInvalidAccess() {
        final String input = "{\"accessStatus\": \"BLOCD\"}";
        final var user = new User(31, "test22", "password", "test", "test", ORGANISATION1, UserRole.USER);
        repository.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, adminAuthToken)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("31/status")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(ResponseStatus.BAD_REQUEST.getMessage()));
        assertEquals(AccessStatus.ALLOWED, user.getAccessStatus());
    }
}
