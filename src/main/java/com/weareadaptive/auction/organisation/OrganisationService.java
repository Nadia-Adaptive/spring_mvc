package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.exception.BusinessException;
import com.weareadaptive.auction.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganisationService {
    private final OrganisationRepository repository;

    public OrganisationService(final OrganisationRepository repository) {
        this.repository = repository;
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
        if (existingOrganisation != null) {
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
