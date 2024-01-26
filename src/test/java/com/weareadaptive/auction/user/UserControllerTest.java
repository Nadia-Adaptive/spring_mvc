package com.weareadaptive.auction.user;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.model.AccessStatus;
import com.weareadaptive.auction.organisation.OrganisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static com.weareadaptive.auction.ControllerTestData.ADMIN_AUTH_TOKEN;

import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORGANISATION2;
import static com.weareadaptive.auction.TestData.ORGANISATION3;
import static com.weareadaptive.auction.TestData.ORG_1;
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

    private static Stream<String> invalidInput() {
        return Stream.of("""
                        { "username": "", "firstName": "test03", "lastName": "test03",
                        "password": "password", "organisation": "Organisation 1", "userRole": "USER" }""",
                """
                        { "username": "test03", "firstName": "", "lastName": "test03",
                            "password": "password", "organisation": "Organisation 1", "userRole": "USER"  }""",
                """
                        { "username": "test03", "firstName": "test03", "lastName": null,
                         "password": "password", "organisation": "Organisation 1", "userRole": "USER" }""",
                """
                        {   "username": "test03", "firstName": "test03",
                            "lastName": "test03", "password": "", "organisation": "Organisation 1", "userRole": "USER" }""",
                """
                        { "username":"test_03", "firstName":"test03", "lastName":"test03",
                         "password":"password", "organisation":"Organisation 1", "userRole": "USER"  }""",
                """
                        { "username":"test_03", "firstName":"test03", "lastName":"test03",
                         "password":"password", "organisation":"Organisation 1", "userRole": ""  }""",
                """
                        { "username":"admin_02", "firstName":"admin2", "lastName":"admin2",
                         "password":"password", "organisation":"Organisation 1", "userRole": "ADMIN"  }""");
    }

    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/users/";
    }

    @Test
    public void getUser_UserExistsAndRoleIsAdmin_ReturnsUserAnd200() {
        final var id = repository.nextId();
        final var user = new User(id, "test01", "password", "test", "test", ORGANISATION1, UserRole.USER);
        repository.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .get(String.valueOf(id)).then()
                .statusCode(HttpStatus.OK.value()).assertThat()
                .body(
                        "id", equalTo(id), "username", equalTo("test01"),
                        "firstName", equalTo("test"), "lastName", equalTo("test"),
                        "organisationName", equalTo(ORG_1), "accessStatus", equalTo("ALLOWED"), "userRole",
                        equalTo("USER"));
    }

    @Test
    public void getUser_UserDoesntExistAndRoleIsAdmin_ReturnsMessageAnd404() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when().get("-1").then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ErrorMessage.NOT_FOUND.getMessage()));
    }

    @Test
    public void AccessUserRoute_RoleIsUser_ReturnsMessageAnd403() {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, testData.user1Token())
                .when().get("3").then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo(ErrorMessage.FORBIDDEN.getMessage()));
    }

    @Test
    public void postUser_createUserWithValidInputs_ReturnsMessageAnd201() {
        final String userInput = """
                {   "username": "test03", "firstName": "test03",
                "lastName": "test03", "password": "invalid",
                "organisationName": "Organisation 1", "userRole": "USER" }""";
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(userInput)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", equalTo(ErrorMessage.CREATED.getMessage()));
    }

    @Test
    public void PostUser_createAdminWithValidInputs_ReturnsMessageAnd201() {
        final String userInput = """
                {   "username": "test05", "firstName": "test03",
                "lastName": "test03", "password": "invalid",
                "organisation": "Organisation 1", "userRole": "USER" }""";
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(userInput)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", equalTo(ErrorMessage.CREATED.getMessage()));
    }

    @Test
    public void PostUser_UserInputIsValid_UserIsAddedToOrganisation() {
        final String userInput = """
                {   "username": "test04", "firstName": "test03",
                "lastName": "test03", "password": "valid",
                "organisationName": "Organisation 1", "userRole": "USER" }""";
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(userInput)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", equalTo(ErrorMessage.CREATED.getMessage()));
    }

    @ParameterizedTest()
    @MethodSource("invalidInput")
    public void PostUser_createUserWithInvalidInputs_ReturnsMessageAnd400(final String input) {
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message",
                        equalTo(ErrorMessage.BAD_REQUEST.getMessage()));
    }

    @Test()
    public void PutUser_UpdateUserWithValidInputs_ReturnsMessageAnd200() {
        final String input = """
                { "organisationName": "%s" }""".formatted(ORGANISATION3.organisationName());
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ErrorMessage.OK.getMessage()));
    }

    @Test()
    public void PutUser_AttemptUpdateNonExistentUser_ReturnsMessageAnd404() {
        final String input = """
                { "organisation": "organisation 123" }""";
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("-1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ErrorMessage.NOT_FOUND.getMessage()));
    }

    @Test()
    public void PutUserStatus_UpdateUserWithValidAccessStatus_ReturnsMessageAnd200() {
        final String input = """
                { "accessStatus": "BLOCKED" }""";
        final var user = new User(30, "test02", "password", "test", "test", ORGANISATION1, UserRole.USER);
        repository.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("30/status")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ErrorMessage.OK.getMessage()));
        assertEquals(AccessStatus.BLOCKED, user.getAccessStatus());
    }

    @Test()
    public void PutUserStatus_UpdateUserWithValidAccessStatus_ReturnsMessageAnd400() {
        final String input = """
                { "accessStatus": "BLOC" }""";
        final var user = new User(31, "test22", "password", "test", "test", ORGANISATION1, UserRole.USER);
        repository.add(user);
        given()
                .baseUri(uri)
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .when()
                .header("Content-Type", "application/json")
                .body(input)
                .put("31/status")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(ErrorMessage.BAD_REQUEST.getMessage()));
        assertEquals(AccessStatus.ALLOWED, user.getAccessStatus());
    }
}