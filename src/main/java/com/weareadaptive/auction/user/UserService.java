package com.weareadaptive.auction.user;

import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.auction.AuctionRepository;
import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.organisation.Organisation;
import com.weareadaptive.auction.organisation.OrganisationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.weareadaptive.auction.utils.StringUtil.isNullOrEmpty;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    private final AuctionRepository auctionRepository;

    public UserService(final UserRepository userRepository, final OrganisationRepository organisationRepository,
                       final AuctionRepository auctionRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.auctionRepository = auctionRepository;
    }

    public User createUser(final String username, final String password, final String firstName, final String lastName,
                           final String organisationName, final UserRole role) {
        if (!username.matches("^[a-zA-Z0-9]*$")) {
            throw new BusinessException("Invalid username");

        }
        if (isNullOrEmpty(organisationName)) {
            throw new BusinessException("Cannot have empty organisation");
        }
        if (role == UserRole.ADMIN && !organisationName.equals("ADMIN")) {
            throw new BusinessException("Admins must be added to correct organisation");
        }
        if (role == UserRole.USER && organisationName.equals("ADMIN")) {
            throw new BusinessException("Users cannot be added to an admin organisation");
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


    public List<User> getUsers() {
        return userRepository.findAll();
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

    public List<Auction> getUserAuctions(final int id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("User does not exist.");
        }
        return auctionRepository.getUserAuctions(id).toList();
    }
}
