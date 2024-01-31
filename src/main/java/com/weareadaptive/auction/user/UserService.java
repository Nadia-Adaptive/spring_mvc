package com.weareadaptive.auction.user;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.organisation.Organisation;
import com.weareadaptive.auction.organisation.OrganisationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    public UserService(final UserRepository userRepository, final OrganisationRepository organisationRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
    }

    public User createUser(final String username, final String password, final String firstName, final String lastName,
                           final String organisationName, final UserRole role) {
        if (!username.matches("^[a-zA-Z0-9]*$") || (role == UserRole.ADMIN && organisationName != "ADMIN")) {

            throw new BusinessException("Invalid parameters.");
        }
        var organisation = organisationRepository.getOrganisationByName(organisationName);

        if (organisation == null) {
            organisation = new Organisation(organisationRepository.nextId(), organisationName, new ArrayList<>());
            organisationRepository.add(organisation);
        }

        User user = new User(
                userRepository.nextId(),
                username, password,
                firstName, lastName,
                organisation, role);
        userRepository.add(user);
        return user;
    }


    public User getUser(final int id) {
        final var user = userRepository.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }
        return user;
    }

    public User updateUser(final int id, final String password, final String firstName,
                           final String lastName, final String organisationName) {
        final var user = userRepository.getUserById(id);
        final var organisation = organisationRepository.getOrganisationByName(organisationName);

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
}
