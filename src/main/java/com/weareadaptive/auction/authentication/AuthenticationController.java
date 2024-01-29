package com.weareadaptive.auction.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.Response;
import com.weareadaptive.auction.security.AuthenticationProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController()
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    ObjectMapper mapper;

    AuthenticationProvider authenticationProvider;

    public AuthenticationController(final AuthenticationProvider authenticationProvider, final ObjectMapper mapper) {
        this.authenticationProvider = authenticationProvider;
        this.mapper = mapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody HashMap<String, String> body) {
        final var auth =
                authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(body.get("username"), body
                        .get("username") + ":" + body.get("password")));

                SecurityContextHolder.getContext().setAuthentication(auth);
        return ResponseEntity.ok().body(new AuthToken(String.valueOf(auth.getCredentials())));
    }
}
