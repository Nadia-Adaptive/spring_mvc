package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(final String username, final String password, final String firstName, final String lastName,
                           final String organisation, final UserRole role) {
        if (username.matches("^[a-zA-Z0-9]*$")) {
            User user = new User(
                    userRepository.nextId(),
                    username, password,
                    firstName, lastName,
                    organisation, role);
            userRepository.add(user);
            return user;
        } else {
            throw new BusinessException("Invalid Username.");
        }
    }

    public User getUser(int id) {
        return userRepository.getUserById(id);
    }

    public User updateUser(final int id, final String username, final String password, final String firstName,
                           final String lastName, final String organisation) {
        final var user = userRepository.getUserById(id);
        if (user != null) {
            user.update(username, password, firstName, lastName, organisation);
        }
        return user;
    }

    public User validateUserCredentials(final String username, final String password) {
        final User user = userRepository.getUserByUsername(username);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        return null;
    }
}
