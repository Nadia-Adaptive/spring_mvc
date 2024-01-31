package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.NotFoundException;
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
    protected void onAdd(final Organisation model) {
        super.onAdd(model);
        organisationNameIndex.put(model.organisationName(), model);
    }

    public void removeUserFromOrganisation(final User u, final String oldOrganisation) {
        final var organisation = organisationNameIndex.get(oldOrganisation);

        System.out.println(organisation);

        if (organisation == null || !organisation.users().contains(u)) {
            throw new NotFoundException("Organisation or user not found");
        }
        organisation.users().remove(u);
    }

    public Organisation getOrganisation(final int id) {
        return getEntity(id);
    }

    public Organisation getOrganisationByName(final String organisationName) {
        return organisationNameIndex.get(organisationName);
    }

}
