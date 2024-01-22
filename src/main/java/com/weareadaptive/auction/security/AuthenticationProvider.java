package com.weareadaptive.auction.security;

import com.weareadaptive.auction.model.UserState;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private UserState userState;

    @Override
    public boolean supports(final Class<?> authentication) {
        return super.supports(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(
            final UserDetails userDetails,
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        System.out.println(userDetails);
    }

    @Override
    protected UserDetails retrieveUser(
            final String userName,
            final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {

        Object token = usernamePasswordAuthenticationToken.getCredentials();
        return Optional
                .ofNullable(token)
                .map(s -> getUser(String.valueOf(s)))
                .orElseThrow();
    }

    private UserDetails getUser(@NotNull final String token) {
        var splitIndex = token.indexOf(":");
        if (splitIndex < 1) {
            throw new BadCredentialsException("Bad token");
        }
        var username = token.substring(0, splitIndex);
        var password = token.substring(splitIndex + 1);
        var user = userState.validateUsernamePassword(username, password);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Bad token");
        }
        return User.builder()
                .username(user.get().getUsername())
                .password(password)
                .roles(user.get().isAdmin() ? "ADMIN" : "USER") // TODO: Refactor to use enum
                // .disabled(user.get().isBlocked())
                .build();
    }

}