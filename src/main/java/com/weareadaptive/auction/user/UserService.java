package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.organisation.Organisation;
import com.weareadaptive.auction.organisation.OrganisationService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationService organisationService;

    public UserService(final UserRepository userRepository, OrganisationService organisationService) {
        this.userRepository = userRepository;
        this.organisationService = organisationService;
    }

    public User createUser(final String username, final String password, final String firstName, final String lastName,
                           final String organisationName, final UserRole role) {
        if (!username.matches("^[a-zA-Z0-9]*$") || (role == UserRole.ADMIN && organisationName != "ADMIN")) {
            //TODO: Deal with magic string
            throw new BusinessException("Invalid parameters.");
        }
        Organisation organisation = null;
        try {
            organisation = organisationService.get(organisationName);
        } catch (final NotFoundException e) {
            organisation = organisationService.addOrganisation(organisationName);
        }

        User user = new User(
                userRepository.nextId(),
                username, password,
                firstName, lastName,
                organisation, role);
        userRepository.add(user);
        organisationService.addUser(user);
        return user;
    }


    public User getUser(int id) {
        final var user = userRepository.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }
        return user;
    }

    public User updateUser(final int id, final String password, final String firstName,
                           final String lastName, final String organisationName) {
        final var user = userRepository.getUserById(id);
        final var organisation = organisationService.get(organisationName);

        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }

        user.update(password, firstName, lastName, organisation);
        return user;
    }

    public User updateUserStatus(final int id, final AccessStatus status) {
        final var user = userRepository.getUserById(id);

        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }

        user.setAccessStatus(status);
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
