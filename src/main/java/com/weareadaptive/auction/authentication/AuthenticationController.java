package com.weareadaptive.auction.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weareadaptive.auction.response.ResponseBody;
import com.weareadaptive.auction.response.ResponseBuilder;
import com.weareadaptive.auction.response.ResponseStatus;
import com.weareadaptive.auction.security.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController()
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    ObjectMapper mapper;
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    AuthenticationProvider authenticationProvider;
    AuthenticationService authService;

    public AuthenticationController(final AuthenticationProvider authenticationProvider, final ObjectMapper mapper,
                                    final AuthenticationService authService) {
        this.authenticationProvider = authenticationProvider;
        this.mapper = mapper;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody> login(@RequestBody final HashMap<String, String> body) {
        Authentication requestAuthentication =
                new UsernamePasswordAuthenticationToken(body.get("username"), body.get("password"));

        final var auth = authenticationProvider.authenticate(requestAuthentication);

        if (!auth.isAuthenticated()) {
            throw new BadCredentialsException(ResponseStatus.BAD_CREDENTIALS.getMessage());
        }

        final var token = authService.generateJWTToken(body.get("username"));
        SecurityContextHolder.getContext().setAuthentication(auth);

        final var role = auth.getAuthorities().toArray()[0].toString().substring("role_".length());


        logger.info("User signed in.");

        System.out.println(role);

        return ResponseBuilder.ok(new AuthenticationDTO(token, body.get("username"), role));
    }
}
