package com.weareadaptive.auction.model;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class UserState extends State<User> {
    private final Map<String, User> usernameIndex;

    public UserState() {
        usernameIndex = new HashMap<>();
    }

    @Override
    protected void onAdd(final User model) {
        if (usernameIndex.containsKey(model.getUsername())) {
            throw new BusinessException(format("Username \"%s\" already exists", model.getUsername()));
        }
        usernameIndex.put(model.getUsername(), model);
    }


    public Optional<User> validateUsernamePassword(final String username, final String password) {
        if (!usernameIndex.containsKey(username)) {
            return Optional.empty();
        }
        var user = usernameIndex.get(username);
        if (!user.validatePassword(password)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public User getUserById(final int id) {
        return usernameIndex.values().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User getUserByUsername(final String username) {
        return usernameIndex.get(username);
    }
}
