package com.weareadaptive.auction.user;

import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.model.State;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Component
public class UserRepository extends State<User> {
    private final Map<String, User> usernameIndex;

    public UserRepository() {
        usernameIndex = new HashMap<>();
    }

    @Override
    protected void onAdd(final User model) {
        if (usernameIndex.containsKey(model.getUsername())) {
            throw new BusinessException(format("Username \"%s\" already exists", model.getUsername()));
        }
        usernameIndex.put(model.getUsername(), model);
    }

    public User getUserById(final int id) {
        return usernameIndex.values().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User getUserByUsername(final String username) {
        return usernameIndex.get(username);
    }

    public List<User> findAll() {
        return stream().toList();
    }
}
