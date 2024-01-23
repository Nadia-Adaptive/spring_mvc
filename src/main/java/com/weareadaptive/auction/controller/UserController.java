package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.Serialiser;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import com.weareadaptive.auction.service.UserService;
import jdk.jfr.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static utils.ResponseUtil.createResponse;

@RestController()
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private final Serialiser serialiser;

    public UserController(final UserService userService, final Serialiser serialiser) {
        this.userService = userService;
        this.serialiser = serialiser;
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
        System.out.println(body);
        try {
            userService.create(body.get("username"), body.get("password"), body.get("firstName"), body.get("lastName"),
                    body.get("organisation"));
            return ResponseEntity.ok().build();
        } catch (final BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
