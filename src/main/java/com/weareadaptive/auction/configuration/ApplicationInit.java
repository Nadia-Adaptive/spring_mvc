package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.organisation.Organisation;
import com.weareadaptive.auction.organisation.OrganisationRepository;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRepository;
import com.weareadaptive.auction.user.UserRole;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ApplicationInit {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    public ApplicationInit(final UserRepository userRepository, final OrganisationRepository organisationRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData() {
        var adminOrganisation = new Organisation(organisationRepository.nextId(), "ADMIN", new ArrayList<>());
        organisationRepository.add(adminOrganisation);

        var admin = new User(
                userRepository.nextId(),
                "ADMIN",
                "adminpassword",
                "admin",
                "admin",
                adminOrganisation,
                UserRole.ADMIN);

        userRepository.add(admin);


    }

}
