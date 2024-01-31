package com.weareadaptive.auction.organisation;

import com.weareadaptive.auction.model.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("GetOrganisation_IdExists_ReturnsOrganisation")
    public void getOrganisation() {
        assertEquals(organisation, repository.getOrganisation(1));
    }

    @Test
    @DisplayName("GetOrganisation_IdDoesNotExists_ReturnsNull")
    public void getOrganisationThatDoesntExist() {
        final var organisation = repository.getOrganisation(-1);
        assertEquals(null, organisation);
    }

    @Test
    @DisplayName("RemoveUser_UserExists_RemovesUserFromOrganisation")
    public void removeUser() {
        repository.removeUserFromOrganisation(USER1, ORG_1);
        assertEquals(0, organisation.users().size());
    }

    @Test
    @DisplayName("RemoveUser_UserDoesNotExist_Throws")
    public void removeUserThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> repository.removeUserFromOrganisation(USER3, ORG_1));
    }

    @Test
    @DisplayName("RemoveUser_OldOrganisationDoesNotExist_Throws")
    public void removeUserFromOrganisationThatDoesNotExist() {
        assertThrows(NotFoundException.class, () -> repository.removeUserFromOrganisation(USER1, ORG_2));
    }

    @Test
    @DisplayName("GetOrganisationByName_OrganisationExists_ReturnsOrganisations")
    public void getOrganisationByName() {
        final var organisation = repository.getOrganisationByName(ORG_1);
        assertEquals(organisation, organisation);
    }

    @Test
    @DisplayName("GetOrganisationByName_OldOrganisationDoesNotExist_ReturnsNull")
    public void getOrganisationByNameDoesNotExist() {
        final var organisation = repository.getOrganisationByName("fail");
        assertEquals(null, organisation);
    }

    @Test
    @DisplayName("GetAllOrganisations_ReturnsOrganisations")
    public void getAllOrganisation() {
        repository.add(ORGANISATION2);
        final var organisations = repository.getAllOrganisations();
        assertEquals(2, organisations.count());
    }
}
