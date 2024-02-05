package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.ControllerTestData;
import com.weareadaptive.auction.bid.BidDTO;
import com.weareadaptive.auction.response.ResponseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.weareadaptive.auction.ControllerTestData.adminAuthToken;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionControllerTest {
    @Autowired
    ControllerTestData testData;

    @LocalServerPort
    private int port;
    private String uri;

    private static String generateInput(final String ownerId, final String product, final String minPrice,
                                        final String quantity) {
        return """
                {   "ownerId": "%s", "product": "%s",
                    "minPrice": "%s", "quantity": "%s"
                }""".formatted(ownerId, product, minPrice, quantity);
    }

    private static Stream<String> invalidInput() {
        return Stream.of(
                generateInput("101", "TEST", "1.0", "10"),
                generateInput("1", "", "1.0", "10"),
                generateInput("1", "TEST", "", "10"),
                generateInput("1", "TEST", "1.0", ""));
    }


    @BeforeEach
    void beforeEach() {
        uri = "http://localhost:" + port + "/api/v1/auctions/";
    }

    @Test
    @DisplayName("when POST /auctions is provided valid json returns a message and 201 http code")
    public void postAuction() {
        // @formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .body(generateInput("2", "test", "1.0", "10"))
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("message", containsString(ResponseStatus.CREATED.getMessage()));
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("invalidInput")
    @DisplayName("when POST /auctions is provided invalid input returns a message and 400 http code")
    public void postAuctionWithInvalidInput(final String input) {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .body(input)
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(ResponseStatus.BAD_REQUEST.getMessage()));
        //@formatter:on
    }

    @Test
    @DisplayName("when GET /auctions is provided a valid id, it returns the auction")
    public void getAuction() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .get("1")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()),
                        "data.id", equalTo(1),
                        "data.product", equalTo("TEST"), "data.minPrice", equalTo(1.0d),
                        "data.quantity", equalTo(10),
                        "data.totalRevenue", equalTo(new BigDecimal("0.0")),
                        "totalQuantitySold", equalTo(0), "data.bids.size()", equalTo(0),
                        "data.winningBids.size()", equalTo(0), "data.losingBids.size()", equalTo(0));
        //@formatter:on
    }

    @Test
    @DisplayName("when GET /auctions is provided an invalid id, it returns 404 and a message")
    public void getAuctionInvalidId() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .get("-1")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ResponseStatus.NOT_FOUND.getMessage()));
        //@formatter:on
    }

    @Test
    @DisplayName("when GET /auctions returns a list of auctions excluding the requester's")
    public void getAllAuctions() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()), "data.size()", equalTo(1));
        //@formatter:on
    }

    @Test
    @DisplayName("when GET /auctions/available returns a list of open auctions belonging to the requester")
    public void getAvailableAuctions() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .get("available")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()));
        //@formatter:on
    }

    @Test
    @DisplayName("when PUT /auctions/{id}/bids make a bid on the auction matching the id")
    public void putAuctionMakeABid() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .body(new BidDTO(0, 2, "product", 10.0, 1))
                .put("1/bids")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()));
        //@formatter:on
    }

    @Test
    @DisplayName("when PUT /{id}/close close the auction matching the id")
    public void putCloseAuction() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .put("1/close")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(ResponseStatus.OK.getMessage()));
        //@formatter:on
    }

    @Test
    @DisplayName("when PUT /{id}/close and auction doesn't exist returns 404 and a message")
    public void putCloseAuctionInvalidAuction() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, adminAuthToken)
        .when()
                .put("-2/close")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ResponseStatus.NOT_FOUND.getMessage()));
        //@formatter:on
    }

    @Test
    @DisplayName("when PUT /{id}/close and user doesn't own resource returns 403 and a message")
    public void putCloseAuctionUserDoesntOwnAuction() {
        //@formatter:off
        given()
                .baseUri(uri)
                .header("Content-Type", "application/json")
                .header(AUTHORIZATION, testData.user1Token())
        .when()
                .put("1/close")
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("message", equalTo(ResponseStatus.FORBIDDEN.getMessage()));
        //@formatter:on
    }
}
