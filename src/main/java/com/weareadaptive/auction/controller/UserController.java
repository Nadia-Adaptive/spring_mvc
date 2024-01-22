package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.model.Serialiser;
import com.weareadaptive.auction.model.UserState;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)

public class UserController {
    private final UserState state;
    private final Serialiser serialiser;

    public UserController(final UserState state, final Serialiser serialiser) {
        this.state = state;
        this.serialiser = serialiser;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable final int id) {
        final var user = state.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User not found.\"}");
        }
        return ResponseEntity.status(HttpStatus.OK).body(serialiser.serialise(user)); // TODO: Handle exception (404)
    }
}
