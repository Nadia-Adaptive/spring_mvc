package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NotFoundException;
import com.weareadaptive.auction.user.UserService;
import com.weareadaptive.auction.user.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganisationService {
    private final OrganisationRepository repository;

    public OrganisationService(final OrganisationRepository repository) {
        this.repository = repository;
    }

    public void addUser(final User user) {
        final var organisation = repository.addUserToOrganisation(user);
        if (organisation == null) {
            throw new NotFoundException("User organisation not found.");
        }
    }

    public List<Organisation> getAll() {
        return repository.getAllOrganisations().toList();
    }

    public Organisation get(final int id) {
        final var organisation = repository.getOrganisation(id);
        if (organisation == null) {
            throw new NotFoundException("Organisation not found");
        }
        return organisation;
    }

    public Organisation get(final String organisationName) {
        final var organisation = repository.getOrganisationByName(organisationName);
        if (organisation == null) {
            throw new NotFoundException("Organisation not found");
        }
        return organisation;
    }

    public Organisation addOrganisation(final String organisationName) {
        final var existingOrganisation = repository.getOrganisationByName(organisationName);
        if(existingOrganisation !=null){
            return existingOrganisation;
        }
        try {
            final var organisation = new Organisation(repository.nextId(), organisationName, new ArrayList<>());
            repository.add(organisation);
            return organisation;
        } catch (final BusinessException ex) {
            return repository.getOrganisationByName(organisationName);
        }
    }
}
