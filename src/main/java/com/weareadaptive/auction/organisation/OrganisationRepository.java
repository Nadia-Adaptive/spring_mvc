package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.State;
import com.weareadaptive.auction.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class OrganisationRepository extends State<Organisation> {
    private final Map<String, Organisation> organisationNameIndex;

    public OrganisationRepository() {
        this.organisationNameIndex = new HashMap<>();
    }

    public Stream<Organisation> getAllOrganisations() {
        return organisationNameIndex.values().stream();
    }

    @Override
    protected void onAdd(Organisation model) {
        super.onAdd(model);
        organisationNameIndex.put(model.organisationName(), model);
    }

    public Organisation addUserToOrganisation(final User u) {
        final var organisation = organisationNameIndex.get(u.getOrganisationName());

        if (organisation != null) {
           organisation.users().add(u);
        }
        return organisation;
    }
    public Organisation removeUserFromOrganisation(final User u, final String oldOrganisation) {
        final var organisation = organisationNameIndex.get(oldOrganisation);

        if (organisation != null && organisation.users().contains(u)) {
           organisation.users().remove(u);
            return organisation;
        }
        return null;
    }

    public Organisation getOrganisation(final int id) {
        return getEntity(id);
    }

    public Organisation getOrganisationByName(final String organisationName) {
        return organisationNameIndex.get(organisationName);
    }

}
