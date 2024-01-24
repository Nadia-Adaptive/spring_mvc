package com.weareadaptive.auction.model;

import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class OrganisationState {
    private final Map<String, Organisation> detailsMap;

    public OrganisationState() {
        this.detailsMap = new HashMap<>();
    }

    public Stream<Organisation> getAllDetails() {
        return detailsMap.values().stream();
    }

    public Stream<String> getAllOrganisations() {
        return detailsMap.keySet().stream();
    }

    public void addUserToOrganisation(final User u) {
        if (u.getUserRole() == UserRole.ADMIN) { // Assuming admins should not be added to an organisation
            return;
        }

        String organisationName = u.getOrganisation();

        if (detailsMap.containsKey(organisationName)) {
            detailsMap.get(organisationName).users().add(u);
        } else {
            var users = new ArrayList<User>();
            users.add(u);
            detailsMap.put(organisationName, new Organisation(organisationName, users));
        }
    }
    public boolean removeUserFromOrganisation(final User u, final String oldOrganisation) {
        if (detailsMap.containsKey(oldOrganisation)) {
            return detailsMap.get(oldOrganisation).users().remove(u);
        }
        return false;
    }
}
