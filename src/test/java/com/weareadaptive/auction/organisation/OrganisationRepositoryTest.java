package com.weareadaptive.auction.organisation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.weareadaptive.auction.TestData.ORGANISATION1;
import static com.weareadaptive.auction.TestData.ORGANISATION2;
import static com.weareadaptive.auction.TestData.ORG_1;
import static com.weareadaptive.auction.TestData.ORG_2;
import static com.weareadaptive.auction.TestData.USER1;
import static com.weareadaptive.auction.TestData.USER3;
import static com.weareadaptive.auction.TestData.USER4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrganisationRepositoryTest {
    private OrganisationRepository repository;

    @BeforeEach
    public void initState() {
        repository = new OrganisationRepository();
        repository.add(ORGANISATION1);
    }

    @Test
    public void GetOrganisation_IdExists_ReturnsOrganisation() {
        final var organisation = repository.getOrganisation(0);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    public void GetOrganisation_IdDoesNotExists_ReturnsNull() {
        final var organisation = repository.getOrganisation(-1);
        assertEquals(null, organisation);
    }

    @Test
    public void AddUserToOrganisation_UserOrganisationExists_ReturnsOrganisation() {
        final var organisation = repository.getOrganisation(0);
        repository.addUserToOrganisation(USER1);
        assertEquals(organisation, organisation);
    }

    @Test
    public void AddUserToOrganisation_UserOrganisationDoesNotExist_ReturnsNull() {
        final var organisation = repository.addUserToOrganisation(USER4);
        assertEquals(null, organisation);
    }

    @Test
    public void RemoveUser_UserExists_ReturnsOrganisation() {
        final var organisation = repository.removeUserFromOrganisation(USER1, ORG_1);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    public void RemoveUser_UserDoesNotExist_ReturnsNull() {
        final var organisation = repository.removeUserFromOrganisation(USER3, ORG_1);
        assertEquals(null, organisation);
    }

    @Test
    public void RemoveUser_OldOrganisationDoesNotExist_ReturnsNull() {
        final var organisation = repository.removeUserFromOrganisation(USER1, ORG_2);
        assertEquals(null, organisation);
    }

    @Test
    public void GetOrganisation_OrganisationExists_ReturnsOrganisations() {
        final var organisation = repository.getOrganisation(0);
        assertEquals(ORGANISATION1, organisation);
    }

    @Test
    public void GetOrganisation_OldOrganisationDoesNotExist_ReturnsNull() {
        final var organisation = repository.getOrganisation(-2);
        assertEquals(null, organisation);
    }

    @Test
    public void GetOrganisationByName_OrganisationExists_ReturnsOrganisations() {
        final var organisation = repository.getOrganisationByName(ORG_1);
        assertEquals(ORGANISATION1, organisation);
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
