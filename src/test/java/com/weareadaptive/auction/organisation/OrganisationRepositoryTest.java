package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.weareadaptive.auction.TestData.ORGANISATION2;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static com.weareadaptive.auction.TestData.USER1;
import static com.weareadaptive.auction.TestData.USER3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrganisationRepositoryTest {
    private OrganisationRepository repository;
    private Organisation organisation;

    @BeforeEach
    public void initState() {
        repository = new OrganisationRepository();
        organisation = new Organisation(1, ORG_1, new ArrayList<>() {{
            add(USER1);
        }});
        repository.add(organisation);
    }

    @Test
    public void GetOrganisation_IdExists_ReturnsOrganisation() {
        assertEquals(organisation, repository.getOrganisation(1));
    }

    @Test
    public void GetOrganisation_IdDoesNotExists_ReturnsNull() {
        final var organisation = repository.getOrganisation(-1);
        assertEquals(null, organisation);
    }

    @Test
    public void RemoveUser_UserExists_RemovesUserFromOrganisation() {

        repository.removeUserFromOrganisation(USER1, ORG_1);
        assertEquals(0, organisation.users().size());
    }

    @Test
    public void RemoveUser_UserDoesNotExist_Throws() {
        assertThrows(NotFoundException.class, () -> repository.removeUserFromOrganisation(USER3, ORG_1));
    }

    @Test
    public void RemoveUser_OldOrganisationDoesNotExist_Throws() {
        assertThrows(NotFoundException.class, () -> repository.removeUserFromOrganisation(USER1, ORG_2));
    }

    @Test
    public void GetOrganisationByName_OrganisationExists_ReturnsOrganisations() {
        final var organisation = repository.getOrganisationByName(ORG_1);
        assertEquals(organisation, organisation);
    }

    @Test
    public void GetOrganisationByName_OldOrganisationDoesNotExist_ReturnsNull() {
        final var organisation = repository.getOrganisationByName("fail");
        assertEquals(null, organisation);
    }

    @Test
    public void GetAllOrganisation_ReturnsOrganisations() {
        repository.add(ORGANISATION2);
        final var organisations = repository.getAllOrganisations();
        assertEquals(2, organisations.count());
    }
}
