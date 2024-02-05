package com.weareadaptive.auction.user;

import com.weareadaptive.auction.authentication.AuthenticationController;
import com.weareadaptive.auction.response.Response;
import com.weareadaptive.auction.response.ResponseBody;
import com.weareadaptive.auction.response.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController()
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseBody> getAllUsers() {
        logger.info("All users requested.");
        final var users = userService.getUsers();

        return ResponseBuilder.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody> getUser(@PathVariable final int id) {
        logger.info("User with id " + id + " requested.");
        final var user = userService.getUser(id);

        return ResponseBuilder.ok(user);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Response> createUser(@RequestBody final HashMap<String, String> body) {
        final var role = UserRole.valueOf(body.get("userRole"));
        logger.info("Request to create user with role " + role.name());

        final var user = userService.createUser(body.get("username"), body.get("password"), body.get("firstName"),
                body.get("lastName"), body.get("organisationName"), role);

        return ResponseBuilder.created();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateUser(@PathVariable final int id, @RequestBody final HashMap<String, String> body) {
        logger.info("Request to update user with id " + id + ".");
        userService.updateUser(id, body.get("password"), body.get("firstName"),
                body.get("lastName"), body.get("organisationName"));

        return ResponseBuilder.ok();
    }

    @PutMapping(value = "/{id}/status")
    public ResponseEntity updateUserAccessStatus(@PathVariable final int id,
                                                 @RequestBody final HashMap<String, String> body) {
        final var accessStatus = AccessStatus.valueOf(body.get("accessStatus"));

        logger.info("Request to update user with id " + id + " permissions to " + accessStatus.name());

        userService.updateUserStatus(id, accessStatus);
        return ResponseBuilder.ok();
    }

    @GetMapping("/{id}/auctions")
    ResponseEntity<ResponseBody> getAuctions(@PathVariable final int id) {
        logger.info("Request to get user with id " + id + " auctions.");
        System.out.println(userService.getUserAuctions(id));
        return ResponseBuilder.ok(userService.getUserAuctions(id));
    }
}
