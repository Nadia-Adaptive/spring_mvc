package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserState userState;

    public UserService(final UserState userState) {
        this.userState = userState;
    }

    public User create(final String username, final String password, final String firstName, final String lastName,
                       final String organisation) {
        User user = new User(
                userState.nextId(),
                username,
                password,
                firstName,
                lastName,
                organisation);
        userState.add(user);
        return user;
    }

    public User getUser(int id) {
        return userState.getUserById(id);
    }

    public User updateUser(final int id, final String username, final String password, final String firstName,
                           final String lastName, final String organisation) {
        final var user = userState.getUserById(id);
        if (user != null) {
            user.update(username, password, firstName, lastName, organisation);
        }
        return user;
    }
}
