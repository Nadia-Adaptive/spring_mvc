package com.weareadaptive.auction.user;

import com.weareadaptive.auction.ErrorMessage;
import com.weareadaptive.auction.ResponseError;
import com.weareadaptive.auction.model.AccessStatus;
import com.weareadaptive.auction.organisation.OrganisationService;
import org.springframework.http.HttpStatus;
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
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable final int id) {
        final var user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/")
    public ResponseEntity createUser(@RequestBody final HashMap<String, String> body) {
        final var user = userService.createUser(body.get("username"), body.get("password"), body.get("firstName"),
                body.get("lastName"), body.get("organisationName"), UserRole.valueOf(body.get("userRole")));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseError(ErrorMessage.CREATED.getMessage())); // TODO: Refactor response messages
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateUser(@PathVariable final int id, @RequestBody final HashMap<String, String> body) {
        userService.updateUser(id, body.get("password"), body.get("firstName"),
                body.get("lastName"), body.get("organisationName"));

        return ResponseEntity.ok().body(new ResponseError(ErrorMessage.OK.getMessage()));
    }

    @PutMapping(value = "/{id}/status")
    public ResponseEntity updateUserAccessStatus(@PathVariable final int id,
                                                 @RequestBody final HashMap<String, String> body) {
        userService.updateUserStatus(id, AccessStatus.valueOf(body.get("accessStatus")));
        return ResponseEntity.ok().body(new ResponseError(ErrorMessage.OK.getMessage()));
    }
}